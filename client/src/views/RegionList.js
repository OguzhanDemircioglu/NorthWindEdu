import React, { useEffect, useReducer, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAdd, faSave, faTrash, faCancel, faSearch, faRotateRight, faArrowsRotate } from "@fortawesome/free-solid-svg-icons";
import { getAllRegions, addRegion, updateRegion, deleteRegion } from "../services/RegionService";

const initialState = [];

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload || [];
        default:
            return state;
    }
}

export default function RegionList() {
    const [regions, dispatch] = useReducer(reducer, initialState);
    const [allData, setAllData] = useState([]);
    const [editing, setEditing] = useState(null);
    const [updateKey, setUpdateKey] = useState(null);
    const [searchText, setSearchText] = useState("");

    const loadData = async () => {
        try {
            const res = await getAllRegions();
            setAllData(res.data || []);
            dispatch({ type: "SET_ALL", payload: res.data || [] });
        } catch (e) {
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    useEffect(() => { loadData(); }, []);

    const handleAdd = () => {
        if (editing) return;
        setEditing({ regionDescription: "" });
        setUpdateKey(null);
    };

    const handleEdit = (item) => {
        setEditing({ ...item });
        setUpdateKey(item.regionId);
    };

    const handleSave = async (item) => {
        try {
            if (!updateKey) {
                await addRegion(item);
            } else {
                await updateRegion(item);
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
        if (window.confirm("Are you sure you want to delete this region?")) {
            try {
                await deleteRegion(id);
                loadData();
            } catch (e) {
                alert(e.message);
            }
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        if (!searchText) {
            dispatch({ type: "SET_ALL", payload: allData });
            return;
        }
        const filtered = allData.filter((r) =>
            Object.values(r).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );
        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3>Regions</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
                <Form.Control
                    type="text"
                    placeholder={`Search`}
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
                    <th>ID</th>
                    <th>Description</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {editing && !updateKey && (
                    <tr>
                        <td>-</td>
                        <td>
                            <input
                                value={editing.regionDescription}
                                onChange={(e) => setEditing({ ...editing, regionDescription: e.target.value })}
                            />
                        </td>
                        <td>
                            <Button variant="primary" size="sm" onClick={() => handleSave(editing)}>
                                <FontAwesomeIcon icon={faSave} />
                            </Button>
                            <Button variant="secondary" size="sm" className="ms-2" onClick={handleCancel}>
                                <FontAwesomeIcon icon={faCancel} />
                            </Button>
                        </td>
                    </tr>
                )}

                {regions.map((r, i) => {
                    const isEditing = updateKey === r.regionId;
                    return (
                        <tr key={i}>
                            <td>{r.regionId}</td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={editing.regionDescription}
                                        onChange={(e) =>
                                            setEditing({ ...editing, regionDescription: e.target.value })
                                        }
                                    />
                                ) : (
                                    r.regionDescription
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <>
                                        <Button variant="primary" size="sm" onClick={() => handleSave(editing)}>
                                            <FontAwesomeIcon icon={faSave} />
                                        </Button>
                                        <Button variant="secondary" size="sm" className="ms-2" onClick={handleCancel}>
                                            <FontAwesomeIcon icon={faCancel} />
                                        </Button>
                                    </>
                                ) : (
                                    <>
                                        <Button
                                            variant="warning"
                                            size="sm"
                                            className="me-2"
                                            onClick={() => handleEdit(r)}
                                        >
                                            <FontAwesomeIcon icon={faArrowsRotate} />
                                        </Button>
                                        <Button variant="danger" size="sm" onClick={() => handleDelete(r.regionId)}>
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