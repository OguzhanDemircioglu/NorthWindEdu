import React, { useEffect, useReducer, useState } from "react";
import {getCustomers, addCustomer, deleteCustomer, updateCustomer,} from "../services/CustomerService";
import { Button, Table, Form } from "react-bootstrap";
import {faAdd, faArrowsRotate, faCancel, faRotateRight, faSave, faSearch, faTrash,} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const initialState = [];

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload;
        default:
            return state;
    }
}

export default function CustomerList() {
    const [customers, dispatch] = useReducer(reducer, initialState);
    const [updateId, setUpdateId] = useState(null);
    const [editingCustomer, setEditingCustomer] = useState(null);
    const [allCustomers, setAllCustomers] = useState([]);
    const [searchColumn, setSearchColumn] = useState(null);
    const [searchText, setSearchText] = useState("");

    const allowedFields = [
        "customerId",
        "companyName",
        "contactName",
        "contactTitle",
        "address",
        "city",
        "region",
        "postalCode",
        "country",
        "phone",
    ];

    const loadCustomers = async () => {
        try {
            const response = await getCustomers();
            const filtered = response.data.map((c) => {
                const obj = {};
                allowedFields.forEach((f) => (obj[f] = c[f]));
                return obj;
            });
            setAllCustomers(filtered);
            dispatch({ type: "SET_ALL", payload: filtered });
        } catch (error) {
            console.error(error);
            setAllCustomers([]);
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    useEffect(() => {
        loadCustomers();
    }, []);

    const handleChange = (field, value) => {
        setEditingCustomer((prev) => ({ ...prev, [field]: value }));
    };

    const handleAdd = () => {
        if (editingCustomer) return;
        setEditingCustomer({
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
        });
        setUpdateId(null);
    };

    const handleCancel = () => {
        setEditingCustomer(null);
        setUpdateId(null);
    };

    const handleSave = async (customer) => {
        try {
            const cleaned = {
                ...customer,
                phone: customer.phone?.replace(/\D/g, ""),
                postalCode: customer.postalCode?.replace(/\D/g, ""),
            };

            if (!updateId) {
                await addCustomer(cleaned);
            } else {
                await updateCustomer(cleaned);
            }

            handleCancel();
            loadCustomers();
        } catch (error) {
            alert(error.message);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Delete this customer?")) {
            try {
                await deleteCustomer(id);
                loadCustomers();
            } catch (error) {
                alert(error.message);
            }
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        if (!searchText) {
            dispatch({ type: "SET_ALL", payload: allCustomers });
            return;
        }

        const filtered = allCustomers.filter((c) =>
            c[searchColumn]?.toString().toLowerCase().includes(searchText.toLowerCase())
        );

        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3>Customers</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
                <Form.Select
                    value={searchColumn}
                    onChange={(e) => setSearchColumn(e.target.value)}
                    style={{ maxWidth: "150px", marginRight: "10px" }}
                >
                    <option value="customerId">ID</option>
                    <option value="companyName">Company</option>
                    <option value="contactName">Contact</option>
                    <option value="city">City</option>
                    <option value="country">Country</option>
                </Form.Select>

                <Form.Control
                    type="text"
                    placeholder={`Search by ${searchColumn}`}
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
                        dispatch({ type: "SET_ALL", payload: allCustomers });
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
                    <th>Company</th>
                    <th>Contact</th>
                    <th>Title</th>
                    <th>Address</th>
                    <th>City</th>
                    <th>Region</th>
                    <th>Postal Code</th>
                    <th>Country</th>
                    <th>Phone</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {editingCustomer && !updateId && (
                    <tr>
                        {allowedFields.map((field) => (
                            <td key={field}>
                                <input
                                    type={field === "phone" || field === "postalCode" ? "tel" : "text"}
                                    value={editingCustomer[field] || ""}
                                    onChange={(e) => handleChange(field, e.target.value)}
                                />
                            </td>
                        ))}
                        <td>
                            <Button
                                variant="primary"
                                size="sm"
                                onClick={() => handleSave(editingCustomer)}
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

                {customers.map((customer) => {
                    const isEditing = updateId === customer.customerId;
                    return (
                        <tr key={customer.customerId}>
                            {allowedFields.map((field) => (
                                <td key={field}>
                                    {isEditing ? (
                                        <input
                                            type={field === "phone" || field === "postalCode" ? "tel" : "text"}
                                            value={editingCustomer[field] || ""}
                                            onChange={(e) => handleChange(field, e.target.value)}
                                        />
                                    ) : (
                                        customer[field]
                                    )}
                                </td>
                            ))}
                            <td>
                                {isEditing ? (
                                    <>
                                        <Button
                                            variant="primary"
                                            size="sm"
                                            onClick={() => handleSave(editingCustomer)}
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
                                    </>
                                ) : (
                                    <>
                                        <Button
                                            variant="warning"
                                            size="sm"
                                            className="me-2"
                                            onClick={() => {
                                                setUpdateId(customer.customerId);
                                                setEditingCustomer({ ...customer });
                                            }}
                                        >
                                            <FontAwesomeIcon icon={faArrowsRotate} />
                                        </Button>
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={() => handleDelete(customer.customerId)}
                                        >
                                            <FontAwesomeIcon icon={faTrash} />
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
    );
}
