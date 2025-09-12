import {useEffect, useReducer, useState} from "react";
import {addOrder, deleteOrder, getOrderById, getOrders, updateOrder} from "../services/OrderService";
import { Button, Table, Form } from "react-bootstrap";

const initialState = [];

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload;
        case "UPDATE_FIELD":
            return state.map(order =>
                order.orderId === action.orderId
                    ? {...order, [action.field]: action.value}
                    : order
            );
        case "ADD_NEW":
            return [action.payload, ...state];
        case "REMOVE_NEW":
            return state.filter(order => order.orderId !== action.orderId);
        default:
            return state;
    }
}

export default function OrderList() {
    const [orders, dispatch] = useReducer(reducer, initialState);
    const [newOrderId, setNewOrderId] = useState(null);
    const [searchId, setSearchId] = useState("");
    const [updateId, setUpdateId] = useState(null);

    const loadOrders = async () => {
        try {
            const response = await getOrders();
            dispatch({ type: "SET_ALL", payload: response.data });
        }catch (error) {
            alert(error.message);
        }
    };

    useEffect(() => {
        loadOrders();
    }, []);

    const handleChange = (orderId, field, value) => {
        dispatch({ type: "UPDATE_FIELD", orderId, field, value });
    };

    const handleAdd = () => {
        const tempId = "new-" + Math.random();
        const newOrder = {
            orderId: tempId,
            customerId: "",
            employeeId: "",
            shipViaId: "",
            orderDate: "",
            requiredDate: "",
            shippedDate: "",
            freight: "",
            shipName: "",
            shipAddress: "",
            shipCity: "",
            shipRegion: "",
            shipPostalCode: "",
            shipCountry: "",
            orderDetails: [],
        };
        dispatch({ type: "ADD_NEW", payload: newOrder });
        setNewOrderId(tempId);
    };

    const handleCancel = () => {
        if (newOrderId) {
            dispatch({ type: "REMOVE_NEW", orderId: newOrderId });
            setNewOrderId(null);
        }
    };

    const handleUpdate = (id) => {
        setUpdateId(id);
    };

    const handleSave = async (order) => {
        try {
            if (order.orderId === newOrderId) {
                await addOrder(order);
                setNewOrderId(null);
            } else {
                await updateOrder(order);
                setUpdateId(null);
            }
            loadOrders();
        }catch (error) {
            alert(error.message);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this order?")) {
            try {
                await deleteOrder(id);
                loadOrders();
            } catch (error) {
                alert(error.message);
            }
        }
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!searchId) {
            loadOrders();
            return;
        }
        try {
            const response = await getOrderById(searchId);
            dispatch({ type: "SET_ALL", payload: response.data ? [response.data] : []});
        }catch (error) {
            alert(error.message);
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    return (
        <div style={{ padding: "20px"}}>
            <h3>Orders</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
                <Form.Control
                    type="number"
                    placeholder="Search By ID"
                    value={searchId}
                    onChange={(e) => setSearchId(e.target.value)}
                    style={{maxWidth: "200px", marginRight: "10px"}}
                />
                <Button type="submit" variant="info">Search</Button>
                <Button
                    variant="secondary"
                    className="ms-2"
                    onClick={() => {
                        setSearchId("");
                        loadOrders();
                    }}
                >
                    Reset
                </Button>
            </Form>

            <Button variant="success" className="mb-3" onClick={handleAdd}>
                + Add Order
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
                    <th>Address</th>
                    <th>City</th>
                    <th>Region</th>
                    <th>Postal Code</th>
                    <th>Country</th>
                    <th>Details</th>
                </tr>
                </thead>
                <tbody>
                {orders.map(order => {
                    const isEditing = updateId === order.orderId || newOrderId === order.orderId;
                    return (
                        <tr key={order.orderId}>
                            <td>{order.orderId.toString().startsWith("new-") ? "-" : order.orderId}</td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.customerId}
                                        onChange={(e) => handleChange(order.orderId, "customerId", e.target.value)}
                                    />
                                ) : order.customerId}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.employeeId}
                                        onChange={(e) => handleChange(order.orderId, "employeeId", e.target.value)}
                                    />
                                ) : order.employeeId}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.shipViaId}
                                        onChange={(e) => handleChange(order.orderId, "shipViaId", e.target.value)}
                                    />
                                ) : order.shipViaId}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.orderDate}
                                        onChange={(e) => handleChange(order.orderId, "orderDate", e.target.value)}
                                    />
                                ) : order.orderDate}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.requiredDate}
                                        onChange={(e) => handleChange(order.orderId, "requiredDate", e.target.value)}
                                    />
                                ) : order.requiredDate}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.shippedDate}
                                        onChange={(e) => handleChange(order.orderId, "shippedDate", e.target.value)}
                                    />
                                ) : order.shippedDate}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.freight}
                                        onChange={(e) => handleChange(order.orderId, "freight", e.target.value)}
                                    />
                                ) : order.freight}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.shipName}
                                        onChange={(e) => handleChange(order.orderId, "shipName", e.target.value)}
                                    />
                                ) : order.shipName}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.shipAddress}
                                        onChange={(e) => handleChange(order.orderId, "shipAddress", e.target.value)}
                                    />
                                ) : order.shipAddress}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.shipCity}
                                        onChange={(e) => handleChange(order.orderId, "shipCity", e.target.value)}
                                    />
                                ) : order.shipCity}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.shipRegion}
                                        onChange={(e) => handleChange(order.orderId, "shipRegion", e.target.value)}
                                    />
                                ) : order.shipRegion}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.shipPostalCode}
                                        onChange={(e) => handleChange(order.orderId, "shipPostalCode", e.target.value)}
                                    />
                                ) : order.shipPostalCode}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={order.shipCountry}
                                        onChange={(e) => handleChange(order.orderId, "shipCountry", e.target.value)}
                                    />
                                ) : order.shipCountry}
                            </td>
                            <td>
                                {order.orderDetails && order.orderDetails.length > 0 ? (
                                    <ul style={{ paddingLeft: "15px", margin: 0 }}>
                                        {order.orderDetails.map((detail, index) => (
                                            <li key={index}>
                                                Product ID: {detail.productId}, Quantity: {detail.quantity}, Price: {detail.unitPrice}, Discount: {detail.discount}
                                            </li>
                                        ))}
                                    </ul>
                                ) : (
                                    "-"
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <>
                                        <Button variant="primary" size="sm" onClick={() => handleSave(order)}>
                                            Save
                                        </Button>
                                        {order.orderId === newOrderId && (
                                            <Button variant="secondary" size="sm" className="ms-2" onClick={handleCancel}>
                                                Cancel
                                            </Button>
                                        )}
                                    </>
                                ) : (
                                    <>
                                        <Button variant="warning" size="sm" className="me-2" onClick={() => handleUpdate(order.orderId)}>
                                            Update
                                        </Button>
                                        <Button variant="danger" size="sm" onClick={() => handleDelete(order.orderId)}>
                                            Delete
                                        </Button>
                                    </>
                                )}
                            </td>
                        </tr>
                    );
                })}
                </tbody>
            </Table>
        </div>
    )
}