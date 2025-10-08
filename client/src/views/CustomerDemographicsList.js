import React, { useEffect, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faAdd, faArrowsRotate, faSave, faTrash, faCancel, faSearch, faRotateRight} from "@fortawesome/free-solid-svg-icons";
import {getAllCustomerDemographics, addCustomerDemographics, updateCustomerDemographics, deleteCustomerDemographics} from "../services/CustomerDemographicsService";

export default function CustomerDemographicsList() {
    const [demographics, setDemographics] = useState([]);
    const [editing, setEditing] = useState(null);
    const [updateKey, setUpdateKey] = useState(null);
    const [searchText, setSearchText] = useState("");
    const [allData, setAllData] = useState([]);

    const loadData = async () => {
        try {
            const res = await getAllCustomerDemographics();
            setDemographics(res.data || []);
            setAllData(res.data || []);
        } catch (e) {
            setDemographics([]);
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    const handleAdd = () => {
        if (editing) return;
        setEditing({ customerTypeId: "", customerDesc: "" });
        setUpdateKey(null);
    };

    const handleEdit = (item) => {
        setEditing({ ...item });
        setUpdateKey(item.customerTypeId);
    };

    const handleSave = async (item) => {
        try {
            if (!updateKey) {
                await addCustomerDemographics(item);
            } else {
                await updateCustomerDemographics(item);
            }
            setEditing(null);
            setUpdateKey(null);
            loadData();
        } catch (e) {
            alert("Save failed: " + e.message);
        }
    };

    const handleCancel = () => {
        setEditing(null);
        setUpdateKey(null);
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete?")) {
            try {
                await deleteCustomerDemographics(id);
                loadData();
            } catch (e) {
                alert(e.message);
            }
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        if (!searchText) {
            setDemographics(allData);
            return;
        }
        const filtered = allData.filter((d) =>
            Object.values(d).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );
        setDemographics(filtered);
    };

    return (
        <div style={{ padding: "20px" }}>
            <div style={{ display: "flex", justifyContent: "center", alignItems: "center", marginBottom: "20px" }}>
                <h3 className="me-2">Customer <br /> Demographics</h3>

                <Form className="d-flex mb-3" onSubmit={handleSearch}>
                    <Form.Control
                        type="text"
                        placeholder="Search"
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
                            setDemographics(allData);
                        }}
                    >
                        <FontAwesomeIcon icon={faRotateRight} />
                    </Button>
                    <Button variant="success" className="ms-2" onClick={handleAdd}>
                        <FontAwesomeIcon icon={faAdd} />
                    </Button>
                </Form>
            </div>

            <div className="table-wrapper" style={{ display: "flex", justifyContent: "center" }}>
                <Table striped bordered hover className="table-compact" style={{ maxWidth: "700px" }}>
                    <thead>
                    <tr>
                        <th className="id-col">ID</th>
                        <th>Description</th>
                        <th className="actions-col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {editing && !updateKey && (
                        <tr>
                            <td>
                                <input
                                    value={editing.customerTypeId}
                                    onChange={(e) =>
                                        setEditing({
                                            ...editing,
                                            customerTypeId: e.target.value
                                        })
                                    }
                                />
                            </td>
                            <td>
                                <input
                                    value={editing.customerDesc}
                                    onChange={(e) =>
                                        setEditing({
                                            ...editing,
                                            customerDesc: e.target.value
                                        })
                                    }
                                />
                            </td>
                            <td>
                                <Button
                                    variant="primary"
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

                    {demographics.map((d, i) => {
                        const isEditing = updateKey === d.customerTypeId;
                        return (
                            <tr key={i}>
                                <td>
                                    {isEditing ? (
                                        <input
                                            value={editing.customerTypeId}
                                            disabled
                                            onChange={(e) =>
                                                setEditing({
                                                    ...editing,
                                                    customerTypeId: e.target.value
                                                })
                                            }
                                        />
                                    ) : (
                                        d.customerTypeId
                                    )}
                                </td>
                                <td>
                                    {isEditing ? (
                                        <input
                                            value={editing.customerDesc}
                                            onChange={(e) =>
                                                setEditing({
                                                    ...editing,
                                                    customerDesc: e.target.value
                                                })
                                            }
                                        />
                                    ) : (
                                        d.customerDesc
                                    )}
                                </td>
                                <td>
                                    {isEditing ? (
                                        <>
                                            <Button
                                                variant="primary"
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
                                        </>
                                    ) : (
                                        <>
                                            <Button
                                                variant="warning"
                                                size="sm"
                                                className="btn-compact me-2"
                                                onClick={() => handleEdit(d)}
                                            >
                                                <FontAwesomeIcon icon={faArrowsRotate} />
                                            </Button>
                                            <Button
                                                variant="danger"
                                                size="sm"
                                                className="btn-compact"
                                                onClick={() => handleDelete(d.customerTypeId)}
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
        </div>
    );
}