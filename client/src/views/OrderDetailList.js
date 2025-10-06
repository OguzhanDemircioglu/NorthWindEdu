import React, { useEffect, useReducer, useState } from "react";
import {addOrderDetail, deleteOrderDetail, getOrderDetails, updateOrderDetail,} from "../services/OrderDetailService";
import { getOrders } from "../services/OrderService";
import { getCustomers } from "../services/CustomerService";
import { getAllProducts } from "../services/ProductService";
import { getEmployees} from "../services/EmployeeService";
import { getAllShippers} from "../services/ShipperService";
import { Button, Table, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faAdd, faArrowsRotate, faCancel, faRotateRight, faSave, faSearch, faTrash,} from "@fortawesome/free-solid-svg-icons";

const initialState = [];

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload || [];
        default:
            return state;
    }
}

export default function OrderDetailList() {
    const [details, dispatch] = useReducer(reducer, initialState);
    const [editingDetail, setEditingDetail] = useState(null);
    const [allDetails, setAllDetails] = useState([]);
    const [updateKey, setUpdateKey] = useState(null);
    const [searchText, setSearchText] = useState("");
    const [orders, setOrders] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [products, setProducts] = useState([]);
    const [employees, setEmployees] = useState([]);
    const [shippers, setShippers] = useState([]);
    const loadDetails = async () => {
        try {
            const response = await getOrderDetails();
            setAllDetails(response.data || []);
            dispatch({ type: "SET_ALL", payload: response.data || [] });
        } catch (error) {
            setAllDetails([]);
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    const loadLookups = async () => {
        try {
            const [order, customer, product, employee, shipper] = await Promise.all([
                getOrders(),
                getCustomers(),
                getAllProducts(),
                getEmployees(),
                getAllShippers(),
            ]);
            setOrders(order.data || []);
            setCustomers(customer.data || []);
            setProducts(product.data || []);
            setEmployees(employee.data || []);
            setShippers(shipper.data || []);
        } catch (err) {
            console.error("Error loading data:", err);
        }
    };

    useEffect(() => {
        loadDetails();
        loadLookups();
    }, []);

    const handleChange = (field, value) => {
        setEditingDetail((prev) => ({ ...prev, [field]: value }));
    };

    const handleAdd = () => {
        if (editingDetail) return;
        setEditingDetail({
            orderId: "",
            productId: "",
            unitPrice: "",
            quantity: "",
            discount: "0",
        });
        setUpdateKey(null);
    };

    const handleEdit = (detail) => {
        setEditingDetail({ ...detail });
        setUpdateKey({ orderId: detail.orderId, productId: detail.productId });
    };

    const handleSave = async (detail) => {
        try {
            if (!updateKey) {
                await addOrderDetail(detail);
            } else {
                await updateOrderDetail(detail);
            }
            setEditingDetail(null);
            setUpdateKey(null);
            loadDetails();
        } catch (err) {
            console.error("Save failed:", err);
            alert("Save failed: " + err.message);
        }
    };

    const handleCancel = () => {
        setEditingDetail(null);
        setUpdateKey(null);
    };

    const handleDelete = async (orderId, productId) => {
        if (window.confirm("Are you sure you want to delete this detail?")) {
            try {
                await deleteOrderDetail(orderId, productId);
                loadDetails();
            } catch (err) {
                console.error("Delete failed:", err);
            }
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        if (!searchText) {
            dispatch({ type: "SET_ALL", payload: allDetails });
            return;
        }

        const filtered = allDetails.filter((detail) =>
            Object.values(detail).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );

        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3>Order Details</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
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
                        dispatch({ type: "SET_ALL", payload: allDetails });
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
                    <th>Order</th>
                    <th>Product</th>
                    <th>Unit Price</th>
                    <th>Quantity</th>
                    <th>Discount</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {editingDetail && (
                    <tr>
                        <td>
                            <Form.Select
                                value={editingDetail.orderId || ""}
                                onChange={(e) => handleChange("orderId", e.target.value)}
                            >
                                <option value="">Select order...</option>
                                {orders.map((o) => {
                                    const customer = customers.find(
                                        (c) => c.customerId === o.customerId
                                    );
                                    const employee = employees.find(
                                        (e) => e.employeeId === o.employeeId
                                    );
                                    const shipper = shippers.find(
                                        (s) => s.shipperId === o.shipViaId
                                    );
                                    return (
                                        <option key={o.orderId} value={o.orderId}>
                                            {o.orderId} - {customer?.contactName || "Unknown"}, {employee?.firstName || "Unknown"} {employee?.lastName || "Unknown"}, {shipper?.companyName || "Unknown"}
                                        </option>
                                    );
                                })}
                            </Form.Select>
                        </td>

                        <td>
                            <Form.Select
                                value={editingDetail.productId || ""}
                                onChange={(e) => handleChange("productId", e.target.value)}
                            >
                                <option value="">Select product...</option>
                                {products.map((p) => (
                                    <option key={p.productId} value={p.productId}>
                                        {p.productName}
                                    </option>
                                ))}
                            </Form.Select>
                        </td>

                        <td>
                            <Form.Control
                                type="number"
                                value={editingDetail.unitPrice || ""}
                                onChange={(e) => handleChange("unitPrice", e.target.value)}
                            />
                        </td>
                        <td>
                            <Form.Control
                                type="number"
                                value={editingDetail.quantity || ""}
                                onChange={(e) => handleChange("quantity", e.target.value)}
                            />
                        </td>
                        <td>
                            <Form.Select
                                value={editingDetail.discount || "0"}
                                onChange={(e) => handleChange("discount", e.target.value)}
                            >
                                <option value="0">0</option>
                                <option value="1">1</option>
                            </Form.Select>
                        </td>

                        <td>
                            <Button
                                variant="primary"
                                size="sm"
                                onClick={() => handleSave(editingDetail)}
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

                {details.map((detail, i) => {
                    const isEditing =
                        updateKey &&
                        updateKey.orderId === detail.orderId &&
                        updateKey.productId === detail.productId;
                    if (isEditing) return null;

                    const order = orders.find((o) => o.orderId === detail.orderId);
                    const customer = customers.find(
                        (c) => c.customerId === order?.customerId
                    );
                    const employee = employees.find(
                        (e) => e.employeeId === order?.employeeId
                    );
                    const shipper = shippers.find(
                        (s) => s.shipperId === order?.shipViaId
                    );
                    const product = products.find(
                        (p) => p.productId === detail.productId
                    );

                    return (
                        <tr key={i}>
                            <td>
                                {order?.orderId} - {customer?.contactName || "Unknown"}, {employee?.firstName || "Unknown"} {employee?.lastName || "Unknown"}, {shipper?.companyName || "Unknown"}
                            </td>
                            <td>{product?.productName || detail.productId}</td>
                            <td>{detail.unitPrice}</td>
                            <td>{detail.quantity}</td>
                            <td>{detail.discount}</td>
                            <td>
                                <Button
                                    variant="warning"
                                    size="sm"
                                    className="me-2"
                                    onClick={() => handleEdit(detail)}
                                >
                                    <FontAwesomeIcon icon={faArrowsRotate} />
                                </Button>
                                <Button
                                    variant="danger"
                                    size="sm"
                                    onClick={() => handleDelete(detail.orderId, detail.productId)}
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
