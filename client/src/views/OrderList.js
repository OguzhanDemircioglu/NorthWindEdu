import React, { useEffect, useReducer, useState } from "react";
import { addOrder, deleteOrder, getOrders, updateOrder } from "../services/OrderService";
import { getCustomers } from "../services/CustomerService";
import { getEmployees } from "../services/EmployeeService";
import { getAllShippers } from "../services/ShipperService";

import { Button, Table, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faAdd, faArrowsRotate, faCancel, faRotateRight, faSave, faSearch, faTrash} from "@fortawesome/free-solid-svg-icons";

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
    const [searchText, setSearchText] = useState("");

    const [customers, setCustomers] = useState([]);
    const [employees, setEmployees] = useState([]);
    const [shippers, setShippers] = useState([]);

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

    const loadLookups = async () => {
        try {
            const [customer, employee, shipper] = await Promise.all([
                getCustomers(),
                getEmployees(),
                getAllShippers(),
            ]);

            setCustomers(customer.data || customer);
            setEmployees(employee.data || employee);
            setShippers(shipper.data || shipper);
        } catch (err) {
            console.error("Cannot retrieve data:", err);
        }
    };

    useEffect(() => {
        loadOrders();
        loadLookups();
    }, []);

    const handleChange = (field, value) => {
        setEditingOrder((prev) => ({ ...prev, [field]: value }));
    };

    const formatDate = (value) => {
        if (!value) return "";
        const d = new Date(value);
        if (isNaN(d)) return value;
        const yyyy = d.getFullYear();
        const mm = String(d.getMonth() + 1).padStart(2, "0");
        const dd = String(d.getDate()).padStart(2, "0");
        return `${yyyy}-${mm}-${dd}`;
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
            Object.values(order).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );
        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <div style={{ textAlign: "center", marginBottom: "20px" }}>
                <h3 style={{color: '#343a40', fontWeight: '600', paddingBottom: '5px', borderBottom: '3px solid #6c757d', textTransform: 'uppercase', letterSpacing: '1.5px', marginBottom: '15px'}}>
                    ORDERS
                </h3>
                <Form className="d-flex justify-content-center mt-3" onSubmit={handleSearch}>
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
                    <Button variant="success" className="ms-3" onClick={handleAdd}>
                        <FontAwesomeIcon icon={faAdd} />
                    </Button>
                </Form>
            </div>

            <div className="table-wrapper" style={{ display: "flex", justifyContent: "center" }}>
                <Table striped bordered hover className="table-compact" style={{ maxWidth: "700px" }}>
                    <thead>
                    <tr>
                        <th className="id-col">ID</th>
                        <th>Customer</th>
                        <th>Employee</th>
                        <th>Shipper</th>
                        <th>Order Date</th>
                        <th>Required Date</th>
                        <th>Shipped Date</th>
                        <th>Freight</th>
                        <th>Ship Name</th>
                        <th>Details</th>
                        <th className="actions-col text-center" style={{ width: '80px' }}>Actions</th>
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
                            ].map((field) => (
                                <td key={field}>
                                    {["orderDate", "requiredDate", "shippedDate"].includes(field) ? (
                                        <input
                                            type="date"
                                            value={formatDate(editingOrder[field]) || ""}
                                            onChange={(e) => handleChange(field, e.target.value)}
                                            style={{ width: '100%' }}
                                        />
                                    ) : ["customerId", "employeeId", "shipViaId"].includes(
                                        field
                                    ) ? (
                                        <Form.Select
                                            value={editingOrder[field] || ""}
                                            onChange={(e) =>
                                                handleChange(field, e.target.value)
                                            }
                                            style={{ minWidth: '100px' }}
                                        >
                                            <option value="">Select...</option>
                                            {field === "customerId" &&
                                                customers.map((c) => (
                                                    <option
                                                        key={c.customerId}
                                                        value={c.customerId}
                                                    >
                                                        {c.contactName || c.customerId}
                                                    </option>
                                                ))}
                                            {field === "employeeId" &&
                                                employees.map((e) => (
                                                    <option
                                                        key={e.employeeId}
                                                        value={e.employeeId}
                                                    >
                                                        {e.firstName} {e.lastName}
                                                    </option>
                                                ))}
                                            {field === "shipViaId" &&
                                                shippers.map((s) => (
                                                    <option
                                                        key={s.shipperId}
                                                        value={s.shipperId}
                                                    >
                                                        {s.companyName}
                                                    </option>
                                                ))}
                                        </Form.Select>
                                    ) : (
                                        <input
                                            value={editingOrder[field] || ""}
                                            onChange={(e) => handleChange(field, e.target.value)}
                                            style={{ width: '100%', minWidth: '50px' }}
                                        />
                                    )}
                                </td>
                            ))}
                            <td>-</td>
                            <td className="text-center">
                                <Button
                                    variant="primary"
                                    size="sm"
                                    className="btn-compact me-2"
                                    onClick={() => handleSave(editingOrder)}
                                >
                                    <FontAwesomeIcon icon={faSave} />
                                </Button>
                                <Button
                                    variant="secondary"
                                    size="sm"
                                    className="btn-compact"
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
                                <td>
                                    {customers.find(
                                        (c) => c.customerId === order.customerId
                                    )?.contactName || order.customerId}
                                </td>
                                <td>
                                    {employees.find(
                                        (e) => e.employeeId === order.employeeId
                                    )
                                        ? `${
                                            employees.find(
                                                (e) =>
                                                    e.employeeId === order.employeeId
                                            ).firstName
                                        } ${
                                            employees.find(
                                                (e) =>
                                                    e.employeeId === order.employeeId
                                            ).lastName
                                        }`
                                        : order.employeeId}
                                </td>
                                <td>
                                    {shippers.find(
                                        (s) => s.shipperId === order.shipViaId
                                    )?.companyName || order.shipViaId}
                                </td>
                                <td>{formatDate(order.orderDate)}</td>
                                <td>{formatDate(order.requiredDate)}</td>
                                <td>{formatDate(order.shippedDate)}</td>
                                <td>{order.freight}</td>
                                <td>{order.shipName}</td>
                                <td>
                                    {order.orderDetails && order.orderDetails.length > 0 ? (
                                        <ul style={{ paddingLeft: "15px", margin: 0 }}>
                                            {order.orderDetails.map((detail, index) => (
                                                <li key={index}>
                                                    Product ID: {detail.productId}, Qty: {detail.quantity},
                                                    Price: ${detail.unitPrice}, Disc: {detail.discount}
                                                </li>
                                            ))}
                                        </ul>
                                    ) : (
                                        "-"
                                    )}
                                </td>
                                <td className="text-center">
                                    <Button
                                        variant="warning"
                                        size="sm"
                                        className="btn-compact me-2"
                                        onClick={() => handleEdit(order)}
                                    >
                                        <FontAwesomeIcon icon={faArrowsRotate} />
                                    </Button>
                                    <Button
                                        variant="danger"
                                        size="sm"
                                        className="btn-compact"
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
        </div>
    );
}