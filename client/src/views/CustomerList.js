import React, { useEffect, useReducer, useState } from "react";
import { getCustomers, addCustomer, deleteCustomer, getCustomerById, updateCustomer } from "../services/CustomerService";
import { Button, Table, Form } from "react-bootstrap";

const initialState = [];

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload;
        case "UPDATE_FIELD":
            return state.map(customer =>
                (customer.rowId || customer.customerId) === action.rowId
                    ? { ...customer, [action.field]: action.value }
                    : customer
            );
        case "ADD_NEW":
            return [action.payload, ...state];
        case "REMOVE_NEW":
            return state.filter(customer => (customer.rowId || customer.customerId) !== action.rowId);
        default:
            return state;
    }
}

export default function CustomerList() {
    const [customers, dispatch] = useReducer(reducer, initialState);
    const [newCustomerId, setNewCustomerId] = useState(null);
    const [searchId, setSearchId] = useState("");
    const [updateId, setUpdateId] = useState(null);

    const loadCustomers = async () => {
        try {
            const response = await getCustomers();
            dispatch({ type: "SET_ALL", payload: response.data });
        } catch (error) {
            alert(error.message);
        }
    };

    useEffect(() => {
        loadCustomers();
    }, []);

    const handleChange = (rowId, field, value) => {
        dispatch({ type: "UPDATE_FIELD", rowId, field, value });
    };

    const handleAdd = () => {
        const tempId = "new-" + Math.random();
        dispatch({
            type: "ADD_NEW",
            payload: {
                rowId: tempId,
                customerId: "",
                companyName: "",
                contactName: "",
                contactTitle: "",
                address: "",
                city: "",
                region: "",
                postalCode: "",
                country: "",
                phone: "",
                fax: ""
            }
        });
        setNewCustomerId(tempId);
    };

    const handleCancel = () => {
        if (newCustomerId) {
            dispatch({ type: "REMOVE_NEW", rowId: newCustomerId });
            setNewCustomerId(null);
        }
    };

    const handleUpdate = (id) => {
        setUpdateId(id);
    };

    const handleSave = async (customer) => {
        try {
            if (customer.rowId === newCustomerId) {
                await addCustomer(customer);
                setNewCustomerId(null);
            } else {
                await updateCustomer(customer);
                setUpdateId(null);
            }
            loadCustomers();
        } catch (error) {
            alert(error.message);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this customer?")) {
            try {
                await deleteCustomer(id);
                loadCustomers();
            } catch (error) {
                alert(error.message);
            }
        }
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!searchId) {
            loadCustomers();
            return;
        }
        try {
            const response = await getCustomerById(searchId);
            dispatch({ type: "SET_CUSTOMERS", payload: response.data ? [response.data] : [] });
        } catch (error) {
            alert(error.message);
            dispatch({ type: "SET_CUSTOMERS", payload: [] });
        }
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3>Customers</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
                <Form.Control
                    type="text"
                    placeholder="Search by ID"
                    value={searchId}
                    onChange={(e) => setSearchId(e.target.value)}
                    style={{ maxWidth: "200px", marginRight: "10px" }}
                />
                <Button type="submit" variant="info">Search</Button>
                <Button
                    variant="secondary"
                    className="ms-2"
                    onClick={() => {
                        setSearchId("");
                        loadCustomers();
                    }}
                >
                    Reset
                </Button>
            </Form>

            <Button
                variant="success"
                className="mb-3"
                onClick={handleAdd}
            >
                + Add Customer
            </Button>

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Company Name</th>
                    <th>Contact Name</th>
                    <th>Contact Title</th>
                    <th>Address</th>
                    <th>City</th>
                    <th>Region</th>
                    <th>Postal Code</th>
                    <th>Country</th>
                    <th>Phone</th>
                    <th>Fax</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {customers.map(customer => {
                    const rowKey = customer.rowId || customer.customerId;
                    const isEditing =
                        updateId === customer.customerId ||
                        newCustomerId === customer.rowId;

                    return (
                        <tr key={rowKey}>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={customer.customerId}
                                        onChange={(e) =>
                                            handleChange(rowKey, "customerId", e.target.value)
                                        }
                                    />
                                ) : (
                                    customer.customerId
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={customer.companyName}
                                        onChange={(e) =>
                                            handleChange(rowKey, "companyName", e.target.value)
                                        }
                                    />
                                ) : (
                                    customer.companyName
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={customer.contactName}
                                        onChange={(e) =>
                                            handleChange(rowKey, "contactName", e.target.value)
                                        }
                                    />
                                ) : (
                                    customer.contactName
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={customer.contactTitle}
                                        onChange={(e) =>
                                            handleChange(rowKey, "contactTitle", e.target.value)
                                        }
                                    />
                                ) : (
                                    customer.contactTitle
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={customer.address}
                                        onChange={(e) =>
                                            handleChange(rowKey, "address", e.target.value)
                                        }
                                    />
                                ) : (
                                    customer.address
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={customer.city}
                                        onChange={(e) =>
                                            handleChange(rowKey, "city", e.target.value)
                                        }
                                    />
                                ) : (
                                    customer.city
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={customer.region}
                                        onChange={(e) =>
                                            handleChange(rowKey, "region", e.target.value)
                                        }
                                    />
                                ) : (
                                    customer.region
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={customer.postalCode}
                                        onChange={(e) =>
                                            handleChange(rowKey, "postalCode", e.target.value)
                                        }
                                    />
                                ) : (
                                    customer.postalCode
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={customer.country}
                                        onChange={(e) =>
                                            handleChange(rowKey, "country", e.target.value)
                                        }
                                    />
                                ) : (
                                    customer.country
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={customer.phone}
                                        onChange={(e) =>
                                            handleChange(rowKey, "phone", e.target.value)
                                        }
                                    />
                                ) : (
                                    customer.phone
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={customer.fax}
                                        onChange={(e) =>
                                            handleChange(rowKey, "fax", e.target.value)
                                        }
                                    />
                                ) : (
                                    customer.fax
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <>
                                        <Button
                                            variant="primary"
                                            size="sm"
                                            onClick={() => handleSave(customer)}
                                            disabled={!customer.customerId}
                                        >
                                            Save
                                        </Button>
                                        {customer.rowId === newCustomerId && (
                                            <Button
                                                variant="secondary"
                                                size="sm"
                                                className="ms-2"
                                                onClick={handleCancel}
                                            >
                                                Cancel
                                            </Button>
                                        )}
                                    </>
                                ) : (
                                    <>
                                        <Button
                                            variant="warning"
                                            size="sm"
                                            className="me-2"
                                            onClick={() => handleUpdate(customer.customerId)}
                                        >
                                            Update
                                        </Button>
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={() => handleDelete(customer.customerId)}
                                        >
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