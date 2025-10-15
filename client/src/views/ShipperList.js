import React, { useEffect, useReducer, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faAdd, faArrowsRotate, faSave, faTrash, faCancel, faSearch, faRotateRight} from "@fortawesome/free-solid-svg-icons";
import {getAllShippers, addShipper, updateShipper, deleteShipper} from "../services/ShipperService";

const initialState = [];

const sortById = (data) => {
    return data.sort((a, b) => a.shipViaId - b.shipViaId);
};

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return sortById(action.payload || []);
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

export default function ShipperList() {
    const [shippers, dispatch] = useReducer(reducer, initialState);
    const [editing, setEditing] = useState(null);
    const [updateKey, setUpdateKey] = useState(null);
    const [allData, setAllData] = useState([]);
    const [searchText, setSearchText] = useState("");

    const loadData = async () => {
        try {
            const res = await getAllShippers();
            setAllData(res.data || []);
            dispatch({ type: "SET_ALL", payload: res.data || [] });
        } catch (e) {
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    const handleChange = (field, value) => {
        if (field === "phone") {
            const digits = value.replace(/\D/g, "").slice(0, 11);
            let formatted = digits;
            if (digits.length > 4)
                formatted = digits.slice(0, 4) + "-" + digits.slice(4);
            if (digits.length > 7)
                formatted =
                    formatted.slice(0, 8) + "-" + digits.slice(7, 9);
            if (digits.length > 9)
                formatted =
                    formatted.slice(0, 11) + "-" + digits.slice(9, 11);
            setEditing((prev) => ({ ...prev, [field]: formatted }));
        } else {
            setEditing((prev) => ({ ...prev, [field]: value }));
        }
    };

    const handleAdd = () => {
        if (editing) return;
        setEditing({
            companyName: "",
            phone: ""
        });
        setUpdateKey(null);
    };

    const handleEdit = (item) => {
        setEditing({ ...item });
        setUpdateKey(item.shipperId);
    };

    const handleSave = async (item) => {
        try {
            const cleaned = {
                ...item,
                phone: item.phone?.replace(/\D/g, "")
            };

            if (!updateKey) {
                await addShipper(cleaned);
            } else {
                await updateShipper(cleaned);
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
        if (window.confirm("Are you sure you want to delete this shipper?")) {
            try {
                await deleteShipper(id);
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
        const filtered = allData.filter((shipper) =>
            Object.values(shipper).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );
        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <div style={{ textAlign: "center", marginBottom: "20px" }}>
                <h3 style={{color: '#343a40', fontWeight: '600', paddingBottom: '5px', borderBottom: '3px solid #6c757d', textTransform: 'uppercase', letterSpacing: '1.5px', marginBottom: '15px'}}>
                    SHIPPERS
                </h3>
                <Form className="d-flex justify-content-center mt-3" onSubmit={handleSearch}>
                    <Form.Control
                        type="text"
                        placeholder="Search"
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
                        <th className="id-col">ID</th>
                        <th>Company Name</th>
                        <th>Phone</th>
                        <th className="actions-col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {editing && !updateKey && (
                        <tr>
                            <td className="id-col text-center">-</td>
                            <td>
                                <input
                                    value={editing.companyName}
                                    onChange={(e) =>
                                        handleChange("companyName", e.target.value)
                                    }
                                    style={{ width: '100%' }}
                                />
                            </td>
                            <td>
                                <input
                                    type="tel"
                                    placeholder="0xxx-xxx-xx-xx"
                                    value={editing.phone}
                                    onChange={(e) => handleChange("phone", e.target.value)}
                                    style={{ width: '100%' }}
                                />
                            </td>
                            <td className="actions-col text-center">
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

                    {shippers.map((shipper) => {
                        const isEditing = updateKey === shipper.shipperId;
                        return (
                            <tr key={shipper.shipperId}>
                                <td className="id-col text-center">{shipper.shipperId}</td>
                                <td>
                                    {isEditing ? (
                                        <input
                                            value={editing.companyName}
                                            onChange={(e) => handleChange("companyName", e.target.value)}
                                            style={{ width: '100%' }}
                                        />
                                    ) : (
                                        shipper.companyName
                                    )}
                                </td>
                                <td>
                                    {isEditing ? (
                                        <input
                                            type="tel"
                                            placeholder="0xxx-xxx-xx-xx"
                                            value={editing.phone}
                                            onChange={(e) => handleChange("phone", e.target.value)}
                                            style={{ width: '100%' }}
                                        />
                                    ) : (
                                        formatPhone(shipper.phone)
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
                                                onClick={() => handleEdit(shipper)}
                                                title="Update"
                                            >
                                                <FontAwesomeIcon icon={faArrowsRotate} />
                                            </Button>
                                            <Button
                                                variant="danger"
                                                size="sm"
                                                className="btn-compact"
                                                onClick={() => handleDelete(shipper.shipperId)}
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