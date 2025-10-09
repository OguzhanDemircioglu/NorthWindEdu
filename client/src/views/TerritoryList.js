import React, { useEffect, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faAdd, faArrowsRotate, faSave, faTrash, faCancel, faSearch, faRotateRight} from "@fortawesome/free-solid-svg-icons";
import {getAllTerritories, addTerritory, updateTerritory, deleteTerritory} from "../services/TerritoryService";
import { getAllRegions } from "../services/RegionService";

export default function TerritoryList() {
    const [territories, setTerritories] = useState([]);
    const [regions, setRegions] = useState([]);
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

    const loadLookups = async () => {
        try {
            const region = await getAllRegions();
            setRegions(region.data || []);
        } catch (err) {
            console.error("Error loading data:", err);
            setRegions([]);
        }
    };

    useEffect(() => {
        loadData();
        loadLookups();
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

    const getRegionName = (id) =>
        regions.find((r) => r.regionId === id)?.regionDescription || "";

    return (
        <div style={{ padding: "20px" }}>
            <div style={{ display: "flex", justifyContent: "center", alignItems: "center", marginBottom: "20px" }}>
                <h3 className="me-2">Territories</h3>

                <div className="d-flex align-items-center">
                    <Form className="d-flex" onSubmit={handleSearch}>
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
                                setTerritories(allData);
                            }}
                        >
                            <FontAwesomeIcon icon={faRotateRight} />
                        </Button>
                    </Form>

                    <Button variant="success" className="ms-3" onClick={handleAdd}>
                        <FontAwesomeIcon icon={faAdd} />
                    </Button>
                </div>
            </div>

            <div className="table-wrapper" style={{ display: "flex", justifyContent: "center" }}>
                <Table striped bordered hover className="table-compact" style={{ maxWidth: "700px" }}>
                    <thead>
                    <tr>
                        <th className="id-col">Territory ID</th>
                        <th>Description</th>
                        <th>Region</th>
                        <th className="actions-col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {territories.map((d, i) => {
                        const isEditing = updateKey === d.territoryId;
                        return (
                            <tr key={i}>
                                <td className="id-col text-center">
                                    {isEditing ? (
                                        <input value={editing.territoryId} disabled style={{ width: '100%' }}/>
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
                                            style={{ width: '100%' }}
                                        />
                                    ) : (
                                        d.territoryDescription
                                    )}
                                </td>
                                <td>
                                    {isEditing ? (
                                        <Form.Select
                                            value={editing.regionId || ""}
                                            onChange={(e) =>
                                                setEditing({ ...editing, regionId: Number(e.target.value) })
                                            }
                                            style={{ width: '100%' }}
                                        >
                                            <option value="">Select region</option>
                                            {regions.map((r) => (
                                                <option key={r.regionId} value={r.regionId}>
                                                    {r.regionDescription}
                                                </option>
                                            ))}
                                        </Form.Select>
                                    ) : (
                                        getRegionName(d.regionId)
                                    )}
                                </td>
                                <td className="actions-col text-center">
                                    {isEditing ? (
                                        <>
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
                            <td className="id-col">
                                <input
                                    value={editing.territoryId}
                                    onChange={(e) =>
                                        setEditing({ ...editing, territoryId: e.target.value })
                                    }
                                    style={{ width: '100%' }}
                                />
                            </td>
                            <td>
                                <input
                                    value={editing.territoryDescription}
                                    onChange={(e) =>
                                        setEditing({ ...editing, territoryDescription: e.target.value })
                                    }
                                    style={{ width: '100%' }}
                                />
                            </td>
                            <td>
                                <Form.Select
                                    value={editing.regionId || ""}
                                    onChange={(e) =>
                                        setEditing({ ...editing, regionId: Number(e.target.value) })
                                    }
                                    style={{ width: '100%' }}
                                >
                                    <option value="">Select...</option>
                                    {regions.map((r) => (
                                        <option key={r.regionId} value={r.regionId}>
                                            {r.regionDescription}
                                        </option>
                                    ))}
                                </Form.Select>
                            </td>
                            <td className="actions-col text-center">
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
                    </tbody>
                </Table>
            </div>
        </div>
    );
}