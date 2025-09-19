import React, { useEffect, useReducer, useState } from "react";
import { addOrder, deleteOrder, getOrders, updateOrder } from "../services/OrderService";
import { Button, Table, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAdd, faArrowsRotate, faCancel, faRotateRight, faSave, faSearch, faTrash } from "@fortawesome/free-solid-svg-icons";

const initialState = [];

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload || [];
        default:
            return state;
    }
}

export default function OrderList() {
    const [orders, dispatch] = useReducer(reducer, initialState);
    const [editingOrder, setEditingOrder] = useState(null);
    const [allOrders, setAllOrders] = useState([]);
    const [updateId, setUpdateId] = useState(null);
    const [searchColumn, setSearchColumn] = useState(null);
    const [searchText, setSearchText] = useState("");

    const loadOrders = async () => {
        try {
            const response = await getOrders();
            setAllOrders(response.data || []);
            dispatch({ type: "SET_ALL", payload: response.data || [] });
        } catch (error) {
            setAllOrders([]);
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    useEffect(() => {
        loadOrders();
    }, []);

    const handleChange = (field, value) => {
        setEditingOrder((prev) => ({ ...prev, [field]: value }));
    };

    const handleDateChange = (field, value) => {
        let digits = value.replace(/\D/g, "");

        if (digits.length > 4) digits = digits.slice(0, 4) + "-" + digits.slice(4);
        if (digits.length > 7) digits = digits.slice(0, 7) + "-" + digits.slice(7, 11);

        setEditingOrder(prev => ({ ...prev, [field]: digits }));
    };

    const formatDate = (dateStr) => {
        if (!dateStr) return "";
        const digits = dateStr.replace(/\D/g, "");
        if (digits.length !== 8) return dateStr;
        return digits.slice(0,4) + "-" + digits.slice(4,6) + "-" + digits.slice(6,8);
    };

    const handleAdd = () => {
        if (editingOrder) return;

        setEditingOrder({
            orderId: null,
            customerId: "",
            employeeId: "",
            shipViaId: "",
            orderDate: "",
            requiredDate: "",
            shippedDate: "",
            freight: "",
            shipName: "",
            shipPostalCode: "",
            shipCountry: "",
            orderDetails: [],
        });
        setUpdateId(null);
    };

    const handleEdit = (order) => {
        setEditingOrder({ ...order });
        setUpdateId(order.orderId);
    };

    const handleSave = async (order) => {
        try {
            if (!updateId) {
                const { orderId, ...newOrder } = order;
                await addOrder(newOrder);
            } else {
                await updateOrder({ ...order, orderId: updateId });
            }

            setEditingOrder(null);
            setUpdateId(null);
            loadOrders();
        } catch (err) {
            console.error("Save failed:", err);
            alert("Save failed: " + err.message);
        }
    };

    const handleCancel = () => {
        setEditingOrder(null);
        setUpdateId(null);
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this order?")) {
            try {
                await deleteOrder(id);
                loadOrders();
            } catch (err) {
                console.error("Delete failed:", err);
            }
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        if (!searchText) {
            dispatch({ type: "SET_ALL", payload: allOrders });
            return;
        }

        const filtered = allOrders.filter((order) =>
            order[searchColumn]
                ?.toString()
                .toLowerCase()
                .includes(searchText.toLowerCase())
        );

        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3>Orders</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
                <Form.Select
                    value={searchColumn}
                    onChange={(e) => setSearchColumn(e.target.value)}
                    style={{ maxWidth: "150px", marginRight: "10px" }}
                >
                    <option value="orderId">Order ID</option>
                    <option value="customerId">Customer</option>
                    <option value="employeeId">Employee</option>
                    <option value="shipViaId">Shipper</option>
                    <option value="shipCountry">Ship Country</option>
                </Form.Select>

                <Form.Control
                    type="text"
                    placeholder={`Search`}
                    value={searchText}
                    onChange={(e) => setSearchText(e.target.value)}
                    style={{ maxWidth: "200px", marginRight: "10px" }}
                />
                <Button type="submit" variant="info">
                    <FontAwesomeIcon icon={faSearch} />
                </Button>
                <Button
                    variant="secondary"
                    className="ms-2"
                    onClick={() => {
                        setSearchText("");
                        dispatch({ type: "SET_ALL", payload: allOrders });
                    }}
                >
                    <FontAwesomeIcon icon={faRotateRight} />
                </Button>
            </Form>

            <Button variant="success" className="mb-3" onClick={handleAdd}>
                <FontAwesomeIcon icon={faAdd} />
            </Button>

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Customer</th>
                    <th>Employee</th>
                    <th>Shipper</th>
                    <th>Order Date</th>
                    <th>Required Date</th>
                    <th>Shipped Date</th>
                    <th>Freight</th>
                    <th>Name</th>
                    <th>Postal Code</th>
                    <th>Country</th>
                    <th>Details</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {editingOrder && (
                    <tr>
                        <td>{editingOrder.orderId || "-"}</td>
                        {[
                            "customerId",
                            "employeeId",
                            "shipViaId",
                            "orderDate",
                            "requiredDate",
                            "shippedDate",
                            "freight",
                            "shipName",
                            "shipPostalCode",
                            "shipCountry",
                        ].map((field) => (
                            <td key={field}>
                                {["orderDate", "requiredDate", "shippedDate"].includes(field) ? (
                                    <input
                                        value={editingOrder[field] || ""}
                                        onChange={(e) => handleDateChange(field, e.target.value)}
                                        placeholder="yyyy-mm-dd"
                                        maxLength={10}
                                    />
                                ) : (
                                    <input
                                        value={editingOrder[field] || ""}
                                        onChange={(e) => handleChange(field, e.target.value)}
                                    />
                                )}
                            </td>
                        ))}
                        <td>-</td>
                        <td>
                            <Button
                                variant="primary"
                                size="sm"
                                onClick={() => handleSave(editingOrder)}
                            >
                                <FontAwesomeIcon icon={faSave} />
                            </Button>
                            <Button
                                variant="secondary"
                                size="sm"
                                className="ms-2"
                                onClick={handleCancel}
                            >
                                <FontAwesomeIcon icon={faCancel} />
                            </Button>
                        </td>
                    </tr>
                )}

                {orders.map((order) => {
                    const isEditing = updateId === order.orderId;
                    if (isEditing) return null;

                    return (
                        <tr key={order.orderId}>
                            <td>{order.orderId}</td>
                            <td>{order.customerId}</td>
                            <td>{order.employeeId}</td>
                            <td>{order.shipViaId}</td>
                            <td>{order.orderDate}</td>
                            <td>{order.requiredDate}</td>
                            <td>{order.shippedDate}</td>
                            <td>{order.freight}</td>
                            <td>{order.shipName}</td>
                            <td>{order.shipPostalCode}</td>
                            <td>{order.shipCountry}</td>
                            <td>
                                {order.orderDetails && order.orderDetails.length > 0 ? (
                                    <ul style={{ paddingLeft: "15px", margin: 0 }}>
                                        {order.orderDetails.map((detail, index) => (
                                            <li key={index}>
                                                Product ID: {detail.productId}, Quantity: {detail.quantity},
                                                Price: {detail.unitPrice}, Discount: {detail.discount}
                                            </li>
                                        ))}
                                    </ul>
                                ) : (
                                    "-"
                                )}
                            </td>
                            <td>
                                <Button
                                    variant="warning"
                                    size="sm"
                                    className="me-2"
                                    onClick={() => handleEdit(order)}
                                >
                                    <FontAwesomeIcon icon={faArrowsRotate} />
                                </Button>
                                <Button
                                    variant="danger"
                                    size="sm"
                                    onClick={() => handleDelete(order.orderId)}
                                >
                                    <FontAwesomeIcon icon={faTrash} />
                                </Button>
                            </td>
                        </tr>
                    );
                })}
                </tbody>
            </Table>
        </div>
    );
}
