import React, { useEffect, useReducer, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faAdd, faSave, faTrash, faCancel, faSearch, faRotateRight} from "@fortawesome/free-solid-svg-icons";
import {getAllCustomerDemos, addCustomerDemo, deleteCustomerDemo} from "../services/CustomerDemoService";

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
    const [editing, setEditing] = useState(null);
    const [allData, setAllData] = useState([]);
    const [searchText, setSearchText] = useState("");
    const [searchColumn, setSearchColumn] = useState("customerId");

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

    useEffect(() => {
        loadData();
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
            demo[searchColumn]
                ?.toString()
                .toLowerCase()
                .includes(searchText.toLowerCase())
        );
        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3>Customer Demographics</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
                <Form.Select
                    value={searchColumn}
                    onChange={(e) => setSearchColumn(e.target.value)}
                    style={{ maxWidth: "180px", marginRight: "10px" }}
                >
                    <option value="customerId">Customer ID</option>
                    <option value="customerTypeId">Customer Type ID</option>
                </Form.Select>

                <Form.Control
                    type="text"
                    placeholder={`Search by ${searchColumn}`}
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
            </Form>

            <Button variant="success" className="mb-3" onClick={handleAdd}>
                <FontAwesomeIcon icon={faAdd} />
            </Button>

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>Customer ID</th>
                    <th>Customer Type ID</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {editing && (
                    <tr>
                        {["customerId", "customerTypeId"].map((field) => (
                            <td key={field}>
                                <input
                                    value={editing[field] || ""}
                                    onChange={(e) => handleChange(field, e.target.value)}
                                />
                            </td>
                        ))}
                        <td>
                            <Button
                                variant="primary"
                                size="sm"
                                onClick={() => handleSave(editing)}
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

                {demos.map((demo, i) => (
                    <tr key={i}>
                        <td>{demo.customerId}</td>
                        <td>{demo.customerTypeId}</td>
                        <td>
                            <Button
                                variant="danger"
                                size="sm"
                                onClick={() =>
                                    handleDelete(demo.customerId, demo.customerTypeId)
                                }
                            >
                                <FontAwesomeIcon icon={faTrash} />
                            </Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </div>
    );
}