import React, { useEffect, useReducer, useState } from "react";
import {getCustomers, addCustomer, deleteCustomer, updateCustomer} from "../services/CustomerService";
import { Button, Table, Form } from "react-bootstrap";
import {faAdd, faArrowsRotate, faCancel, faRotateRight, faSave, faSearch, faTrash} from "@fortawesome/free-solid-svg-icons";
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

const formatPhone = (phone) => {
    if (!phone) return "";
    const digits = phone.replace(/\D/g, "");
    if (digits.length < 11) return phone;
    return digits.replace(/(\d{4})(\d{3})(\d{2})(\d{2})/, "$1-$2-$3-$4");
};

export default function CustomerList() {
    const [customers, dispatch] = useReducer(reducer, initialState);
    const [updateId, setUpdateId] = useState(null);
    const [editingCustomer, setEditingCustomer] = useState(null);
    const [allCustomers, setAllCustomers] = useState([]);
    const [searchText, setSearchText] = useState("");

    const allFields = [
        "customerId",
        "companyName",
        "contactName",
        "contactTitle",
        "phone",
    ];

    const nonIdFields = allFields.filter(f => f !== "customerId");

    const fieldHeaders = ["ID", "Company", "Contact", "Title", "Phone"];


    const loadCustomers = async () => {
        try {
            const response = await getCustomers();
            const filtered = response.data.map((c) => {
                const obj = {};
                allFields.forEach((f) => (obj[f] = c[f]));
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
        if (field === "phone") {
            const digits = value.replace(/\D/g, "").slice(0, 11);
            let formatted = digits;
            if (digits.length > 4) formatted = digits.slice(0, 4) + "-" + digits.slice(4);
            if (digits.length > 7) formatted = formatted.slice(0, 8) + "-" + digits.slice(7, 9);
            if (digits.length > 9) formatted = formatted.slice(0, 11) + "-" + digits.slice(9, 11);
            setEditingCustomer(prev => ({ ...prev, [field]: formatted }));
        } else {
            setEditingCustomer((prev) => ({ ...prev, [field]: value }));
        }
    };

    const handleAdd = () => {
        if (editingCustomer) return;
        setEditingCustomer({
            customerId: "",
            companyName: "",
            contactName: "",
            contactTitle: "",
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
            Object.values(c).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );

        dispatch({ type: "SET_ALL", payload: filtered });
    };

    const getTableHeaders = () => {
        let headers = [];

        if (editingCustomer) {
            headers = fieldHeaders;
        } else {
            headers = fieldHeaders.filter(h => h !== "ID");
        }

        headers.push("Actions");
        return headers;
    };


    return (
        <div style={{ padding: "20px" }}>
            <div style={{ textAlign: "center", marginBottom: "20px" }}>
                <h3 style={{color: '#343a40', fontWeight: '600', paddingBottom: '5px', borderBottom: '3px solid #6c757d', textTransform: 'uppercase', letterSpacing: '1.5px', marginBottom: '15px'}}>
                    CUSTOMERS
                </h3>

                <Form className="d-flex justify-content-center mt-3" onSubmit={handleSearch}>
                    <Form.Control
                        type="text"
                        placeholder={`Search`}
                        value={searchText}
                        onChange={(e) => setSearchText(e.target.value)}
                        style={{ maxWidth: "250px", marginRight: "10px" }}
                    />
                    <Button type="submit" variant="info" title="Search">
                        <FontAwesomeIcon icon={faSearch} />
                    </Button>
                    <Button
                        variant="secondary"
                        className="ms-2"
                        onClick={() => {
                            setSearchText("");
                            dispatch({ type: "SET_ALL", payload: allCustomers });
                        }}
                        title="Reset"
                    >
                        <FontAwesomeIcon icon={faRotateRight} />
                    </Button>
                    <Button variant="success" className="ms-2" onClick={handleAdd} title="Add">
                        <FontAwesomeIcon icon={faAdd} />
                    </Button>
                </Form>
            </div>

            <div className="table-wrapper" style={{ display: "flex", justifyContent: "center" }}>
                <Table striped bordered hover className="table-compact" style={{ maxWidth: "700px" }}>
                    <thead>
                    <tr>
                        {getTableHeaders().map((header) => (
                            <th
                                key={header}
                                className={header === "Actions" ? "actions-col" : ""}
                            >
                                {header}
                            </th>
                        ))}
                    </tr>
                    </thead>
                    <tbody>
                    {editingCustomer && !updateId && (
                        <tr>
                            <td key="customerId" style={{ width: '75px' }}>
                                <input
                                    value={editingCustomer["customerId"] || ""}
                                    onChange={(e) => handleChange("customerId", e.target.value)}
                                    style={{ width: '100%', maxWidth: '75px' }}
                                />
                            </td>
                            {nonIdFields.map((field) => (
                                <td key={field}>
                                    <input
                                        type={field === "phone" ? "tel" : "text"}
                                        value={editingCustomer[field] || ""}
                                        placeholder={field === "phone" ? "0xxx-xxx-xx-xx" : ""}
                                        onChange={(e) => handleChange(field, e.target.value)}
                                        style={{ width: '100%' }}
                                    />
                                </td>
                            ))}
                            <td>
                                <Button
                                    variant="primary"
                                    size="sm"
                                    className="btn-compact me-2"
                                    onClick={() => handleSave(editingCustomer)}
                                    title="Save"
                                >
                                    <FontAwesomeIcon icon={faSave} />
                                </Button>
                                <Button
                                    variant="secondary"
                                    size="sm"
                                    className="btn-compact"
                                    onClick={handleCancel}
                                    title="Cancel"
                                >
                                    <FontAwesomeIcon icon={faCancel} />
                                </Button>
                            </td>
                        </tr>
                    )}

                    {customers.map((customer) => {
                        const isEditing = updateId === customer.customerId;

                        const rowCells = [];

                        if (isEditing) {
                            rowCells.push(
                                <td key="customerId" style={{ width: '75px' }}>
                                    <input
                                        value={editingCustomer.customerId}
                                        disabled
                                        style={{ width: '100%', maxWidth: '75px' }}
                                    />
                                </td>
                            );
                        } else if (editingCustomer) {
                            rowCells.push(<td key="empty-id-cell" style={{ width: '50px' }}></td>);
                        }

                        nonIdFields.forEach((field) => {
                            rowCells.push(
                                <td key={field}>
                                    {isEditing ? (
                                        <input
                                            type={field === "phone" ? "tel" : "text"}
                                            value={editingCustomer[field] || ""}
                                            onChange={(e) => handleChange(field, e.target.value)}
                                            style={{ width: '100%' }}
                                        />
                                    ) : field === "phone" ? (
                                        formatPhone(customer[field])
                                    ) : (
                                        customer[field]
                                    )}
                                </td>
                            );
                        });

                        rowCells.push(
                            <td key="actions-cell">
                                {isEditing ? (
                                    <>
                                        <Button variant="primary" size="sm" className="btn-compact me-2" onClick={() => handleSave(editingCustomer)} title="Save">
                                            <FontAwesomeIcon icon={faSave} />
                                        </Button>
                                        <Button variant="secondary" size="sm" className="btn-compact" onClick={handleCancel} title="Cancel">
                                            <FontAwesomeIcon icon={faCancel} />
                                        </Button>
                                    </>
                                ) : (
                                    <>
                                        <Button variant="warning" size="sm" className="btn-compact me-2" onClick={() => { setUpdateId(customer.customerId); setEditingCustomer({ ...customer }); }} title="Update">
                                            <FontAwesomeIcon icon={faArrowsRotate} />
                                        </Button>
                                        <Button variant="danger" size="sm" className="btn-compact" onClick={() => handleDelete(customer.customerId)} title="Delete">
                                            <FontAwesomeIcon icon={faTrash} />
                                        </Button>
                                    </>
                                )}
                            </td>
                        );

                        return <tr key={customer.customerId}>{rowCells}</tr>;
                    })}
                    </tbody>
                </Table>
            </div>
        </div>
    );
}