import React, { useEffect, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAdd, faArrowsRotate, faSave, faTrash, faCancel, faSearch, faRotateRight } from "@fortawesome/free-solid-svg-icons";
import { getAllTerritories, addTerritory, updateTerritory, deleteTerritory } from "../services/TerritoryService";

export default function TerritoryList() {
    const [territories, setTerritories] = useState([]);
    const [editing, setEditing] = useState(null);
    const [updateKey, setUpdateKey] = useState(null);
    const [searchText, setSearchText] = useState("");
    const [allData, setAllData] = useState([]);

    const loadData = async () => {
        try {
            const res = await getAllTerritories();
            setTerritories(res.data || []);
            setAllData(res.data || []);
        } catch (e) {
            setTerritories([]);
            setAllData([]);
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    const handleAdd = () => {
        if (editing) return;
        setEditing({ territoryId: "", territoryDescription: "", regionId: "" });
        setUpdateKey(null);
    };

    const handleEdit = (item) => {
        setEditing({ ...item });
        setUpdateKey(item.territoryId);
    };

    const handleSave = async (item) => {
        try {
            if (!updateKey) {
                await addTerritory(item);
            } else {
                await updateTerritory(item);
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
                await deleteTerritory(id);
                loadData();
            } catch (e) {
                alert(e.message);
            }
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        if (!searchText) {
            setTerritories(allData);
            return;
        }
        const filtered = allData.filter((d) =>
            Object.values(d).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );
        setTerritories(filtered);
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3>Territories</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
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
                        setTerritories(allData);
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
                    <th>Territory ID</th>
                    <th>Description</th>
                    <th>Region ID</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {territories.map((d, i) => {
                    const isEditing = updateKey === d.territoryId;
                    return (
                        <tr key={i}>
                            <td>
                                {isEditing ? (
                                    <input value={editing.territoryId} disabled />
                                ) : (
                                    d.territoryId
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={editing.territoryDescription}
                                        onChange={(e) =>
                                            setEditing({ ...editing, territoryDescription: e.target.value })
                                        }
                                    />
                                ) : (
                                    d.territoryDescription
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        type="number"
                                        value={editing.regionId || ""}
                                        onChange={(e) =>
                                            setEditing({ ...editing, regionId: Number(e.target.value) })
                                        }
                                    />
                                ) : (
                                    d.regionId
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <>
                                        <Button variant="primary" size="sm" onClick={() => handleSave(editing)}>
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
                                            onClick={() => handleEdit(d)}
                                        >
                                            <FontAwesomeIcon icon={faArrowsRotate} />
                                        </Button>
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={() => handleDelete(d.territoryId)}
                                        >
                                            <FontAwesomeIcon icon={faTrash} />
                                        </Button>
                                    </>
                                )}
                            </td>
                        </tr>
                    );
                })}

                {editing && !updateKey && (
                    <tr>
                        <td>
                            <input
                                value={editing.territoryId}
                                onChange={(e) =>
                                    setEditing({ ...editing, territoryId: e.target.value })
                                }
                            />
                        </td>
                        <td>
                            <input
                                value={editing.territoryDescription}
                                onChange={(e) =>
                                    setEditing({ ...editing, territoryDescription: e.target.value })
                                }
                            />
                        </td>
                        <td>
                            <input
                                type="number"
                                value={editing.regionId || ""}
                                onChange={(e) =>
                                    setEditing({ ...editing, regionId: Number(e.target.value) })
                                }
                            />
                        </td>
                        <td>
                            <Button variant="primary" size="sm" onClick={() => handleSave(editing)}>
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
                </tbody>
            </Table>
        </div>
    );
}