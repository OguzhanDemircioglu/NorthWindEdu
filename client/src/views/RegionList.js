import React, { useEffect, useReducer, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAdd, faArrowsRotate, faSave, faTrash, faCancel, faSearch, faRotateRight } from "@fortawesome/free-solid-svg-icons";
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
            <div style={{ textAlign: "center", marginBottom: "20px" }}>
                <h3 style={{color: '#343a40', fontWeight: '600', paddingBottom: '5px', borderBottom: '3px solid #6c757d', textTransform: 'uppercase', letterSpacing: '1.5px', marginBottom: '15px'}}>
                    REGIONS
                </h3>
                <Form className="d-flex justify-content-center mt-3" onSubmit={handleSearch}>
                    <Form.Control
                        type="text"
                        placeholder={`Search`}
                        value={searchText}
                        onChange={(e) => setSearchText(e.target.value)}
                        style={{ maxWidth: "200px", marginRight: "10px" }}
                    />
                    <Button type="submit" variant="info" title="Search">
                        <FontAwesomeIcon icon={faSearch} />
                    </Button>
                    <Button
                        variant="secondary"
                        className="ms-2"
                        onClick={() => {
                            setSearchText("");
                            dispatch({ type: "SET_ALL", payload: allData });
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
                        <th className="id-col">-</th>
                        <th>Description</th>
                        <th className="actions-col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {editing && !updateKey && (
                        <tr>
                            <td className="text-center id-col">{"-"}</td>
                            <td>
                                <input
                                    value={editing.regionDescription}
                                    onChange={(e) => setEditing({ ...editing, regionDescription: e.target.value })}
                                    style={{ width: '100%' }}
                                />
                            </td>
                            <td className="text-center">
                                <Button
                                    variant="primary"
                                    size="sm"
                                    className="btn-compact me-2"
                                    onClick={() => handleSave(editing)}
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

                    {regions.map((r, index) => {
                        const isEditing = updateKey === r.regionId;
                        return (
                            <tr key={r.regionId}>
                                <td className="text-center id-col">{index + 1}</td>
                                <td>
                                    {isEditing ? (
                                        <input
                                            value={editing.regionDescription}
                                            onChange={(e) =>
                                                setEditing({ ...editing, regionDescription: e.target.value })
                                            }
                                            style={{ width: '100%' }}
                                        />
                                    ) : (
                                        r.regionDescription
                                    )}
                                </td>
                                <td className="text-center">
                                    {isEditing ? (
                                        <>
                                            <Button
                                                variant="primary"
                                                size="sm" className="btn-compact me-2"
                                                onClick={() => handleSave(editing)}
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
                                        </>
                                    ) : (
                                        <>
                                            <Button
                                                variant="warning"
                                                size="sm"
                                                className="btn-compact me-2"
                                                onClick={() => handleEdit(r)}
                                                title="Update"
                                            >
                                                <FontAwesomeIcon icon={faArrowsRotate} />
                                            </Button>
                                            <Button
                                                variant="danger"
                                                size="sm"
                                                className="btn-compact"
                                                onClick={() => handleDelete(r.regionId)}
                                                title="Delete"
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