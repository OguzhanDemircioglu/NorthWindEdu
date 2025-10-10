import React, { useEffect, useReducer, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faAdd, faSave, faTrash, faCancel, faSearch, faRotateRight,} from "@fortawesome/free-solid-svg-icons";
import {getAllCustomerDemos, addCustomerDemo, deleteCustomerDemo,} from "../services/CustomerDemoService";
import { getCustomers } from "../services/CustomerService";
import { getAllCustomerDemographics } from "../services/CustomerDemographicsService";

const initialState = [];

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload || [];
        default:
            return state;
    }
}

export default function CustomerDemoList() {
    const [demos, dispatch] = useReducer(reducer, initialState);
    const [allData, setAllData] = useState([]);
    const [editing, setEditing] = useState(null);
    const [searchText, setSearchText] = useState("");
    const [customers, setCustomers] = useState([]);
    const [demographics, setDemographics] = useState([]);
    const loadData = async () => {
        try {
            const res = await getAllCustomerDemos();
            setAllData(res.data || []);
            dispatch({ type: "SET_ALL", payload: res.data || [] });
        } catch (e) {
            setAllData([]);
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    const loadLookups = async () => {
        try {
            const [customer, demographic] = await Promise.all([
                getCustomers(),
                getAllCustomerDemographics(),
            ]);
            setCustomers(customer.data || []);
            setDemographics(demographic.data || []);
        } catch (err) {
            console.error("Error loading dependencies:", err);
        }
    };

    useEffect(() => {
        loadData();
        loadLookups();
    }, []);

    const handleChange = (field, value) => {
        setEditing((prev) => ({ ...prev, [field]: value }));
    };

    const handleAdd = () => {
        if (editing) return;
        setEditing({ customerId: "", customerTypeId: "" });
    };

    const handleSave = async (demo) => {
        try {
            await addCustomerDemo(demo);
            setEditing(null);
            loadData();
        } catch (err) {
            console.error("Save failed:", err);
            alert("Save failed: " + err.message);
        }
    };

    const handleCancel = () => {
        setEditing(null);
    };

    const handleDelete = async (customerId, customerTypeId) => {
        if (window.confirm("Are you sure you want to delete this record?")) {
            try {
                await deleteCustomerDemo(customerId, customerTypeId);
                loadData();
            } catch (err) {
                console.error("Delete failed:", err);
                alert(err.message);
            }
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        if (!searchText) {
            dispatch({ type: "SET_ALL", payload: allData });
            return;
        }
        const filtered = allData.filter((demo) =>
            Object.values(demo).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );
        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <div style={{ textAlign: "center", marginBottom: "20px" }}>
                <h3 style={{color: '#343a40', fontWeight: '600', paddingBottom: '5px', borderBottom: '3px solid #6c757d', textTransform: 'uppercase', letterSpacing: '1.5px', marginBottom: '15px'}}>
                    CUSTOMER DEMOS
                </h3>

                <Form className="d-flex justify-content-center mt-3" onSubmit={handleSearch}>
                    <Form.Control
                        type="text"
                        placeholder="Search"
                        value={searchText}
                        onChange={(e) => setSearchText(e.target.value)}
                        style={{ maxWidth: "250px", marginRight: "10px" }}
                    />
                    <Button type="submit" variant="info">
                        <FontAwesomeIcon icon={faSearch} />
                    </Button>
                    <Button
                        variant="secondary"
                        className="ms-2"
                        onClick={() => {
                            setSearchText("");
                            dispatch({ type: "SET_ALL", payload: allData });
                        }}
                    >
                        <FontAwesomeIcon icon={faRotateRight} />
                    </Button>
                    <Button
                        variant="success"
                        className="ms-2"
                        onClick={handleAdd}
                    >
                        <FontAwesomeIcon icon={faAdd} />
                    </Button>
                </Form>
            </div>

            <div className="table-wrapper" style={{ display: "flex", justifyContent: "center" }}>
                <Table
                    striped bordered hover
                    className="table-compact"
                    style={{ maxWidth: "800px" }}
                >
                    <thead>
                    <tr>
                        <th>Customer</th>
                        <th>Customer Type</th>
                        <th className="actions-col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {editing && (
                        <tr>
                            <td>
                                <Form.Select
                                    value={editing.customerId || ""}
                                    onChange={(e) => handleChange("customerId", e.target.value)}
                                >
                                    <option value="">Select...</option>
                                    {customers.map((c) => (
                                        <option key={c.customerId} value={c.customerId}>
                                            {c.contactName}
                                        </option>
                                    ))}
                                </Form.Select>
                            </td>
                            <td>
                                <Form.Select
                                    value={editing.customerTypeId || ""}
                                    onChange={(e) => handleChange("customerTypeId", e.target.value)}
                                >
                                    <option value="">Select...</option>
                                    {demographics.map((d) => (
                                        <option key={d.customerTypeId} value={d.customerTypeId}>
                                            {d.customerDesc}
                                        </option>
                                    ))}
                                </Form.Select>
                            </td>
                            <td>
                                <Button
                                    variant="primary"
                                    size="sm"
                                    className="btn-compact me-2"
                                    onClick={() => handleSave(editing)}
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

                    {demos.map((demo, i) => {
                        const customer = customers.find((c) => c.customerId === demo.customerId);
                        const demographic = demographics.find(
                            (d) => d.customerTypeId === demo.customerTypeId
                        );

                        return (
                            <tr key={i}>
                                <td>{customer?.contactName || demo.customerId}</td>
                                <td>{demographic?.customerDesc || demo.customerTypeId}</td>
                                <td>
                                    <Button
                                        variant="danger"
                                        size="sm"
                                        className="btn-compact"
                                        onClick={() =>
                                            handleDelete(demo.customerId, demo.customerTypeId)
                                        }
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