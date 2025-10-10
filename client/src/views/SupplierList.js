import React, { useEffect, useReducer, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    faAdd,
    faArrowsRotate,
    faSave,
    faTrash,
    faCancel,
    faSearch,
    faRotateRight,
} from "@fortawesome/free-solid-svg-icons";
import {
    getAllSuppliers,
    addSupplier,
    updateSupplier,
    deleteSupplier,
} from "../services/SupplierService";

const initialState = [];

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload || [];
        default:
            return state;
    }
}

const formatPhone = (val) => {
    if (!val) return "";
    const digits = val.replace(/\D/g, "");
    if (digits.length < 11) return val;
    return digits.replace(/(\d{4})(\d{3})(\d{2})(\d{2})/, "$1-$2-$3-$4");
};

export default function SupplierList() {
    const [suppliers, dispatch] = useReducer(reducer, initialState);
    const [editing, setEditing] = useState(null);
    const [updateKey, setUpdateKey] = useState(null);
    const [allData, setAllData] = useState([]);
    const [searchText, setSearchText] = useState("");

    const loadData = async () => {
        try {
            const res = await getAllSuppliers();
            setAllData(res.data || []);
            dispatch({ type: "SET_ALL", payload: res.data || [] });
        } catch (e) {
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    const handleAdd = () => {
        if (editing) return;
        setEditing({
            companyName: "",
            contactName: "",
            contactTitle: "",
            city: "",
            postalCode: "",
            phone: "",
        });
        setUpdateKey(null);
    };

    const handleEdit = (item) => {
        setEditing({ ...item });
        setUpdateKey(item.supplierId);
    };

    const handleSave = async (item) => {
        try {
            const cleaned = {
                ...item,
                phone: item.phone?.replace(/\D/g, ""),
                postalCode: item.postalCode?.replace(/\D/g, ""),
            };

            if (!updateKey) {
                await addSupplier(cleaned);
            } else {
                await updateSupplier(cleaned);
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
        if (window.confirm("Are you sure you want to delete this supplier?")) {
            try {
                await deleteSupplier(id);
                loadData();
            } catch (e) {
                alert(e.message);
            }
        }
    };

    const handleChange = (field, value) => {
        if (field === "phone") {
            const digits = value.replace(/\D/g, "").slice(0, 11);
            let formatted = digits;
            if (digits.length > 4) formatted = digits.slice(0, 4) + "-" + digits.slice(4);
            if (digits.length > 7) formatted = formatted.slice(0, 8) + "-" + digits.slice(7, 9);
            if (digits.length > 9) formatted = formatted.slice(0, 11) + "-" + digits.slice(9, 11);
            setEditing((prev) => ({ ...prev, [field]: formatted }));
        } else if (field === "postalCode") {
            const digits = value.replace(/\D/g, "");
            setEditing((prev) => ({ ...prev, [field]: digits }));
        } else {
            setEditing((prev) => ({ ...prev, [field]: value }));
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        if (!searchText) {
            dispatch({ type: "SET_ALL", payload: allData });
            return;
        }
        const filtered = allData.filter((s) =>
            Object.values(s).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );
        dispatch({ type: "SET_ALL", payload: filtered });
    };

    const fields = [
        { label: "ID", key: "supplierId", className: "id-col text-center" },
        { label: "Company", key: "companyName" },
        { label: "Contact", key: "contactName" },
        { label: "Title", key: "contactTitle" },
        { label: "City", key: "city" },
        { label: "Postal Code", key: "postalCode" },
        { label: "Phone", key: "phone" },
    ];

    return (
        <div style={{ padding: "20px" }}>
            <div style={{ textAlign: "center", marginBottom: "20px" }}>
                <h3 style={{color: '#343a40', fontWeight: '600', paddingBottom: '5px', borderBottom: '3px solid #6c757d', textTransform: 'uppercase', letterSpacing: '1.5px', marginBottom: '15px'}}>
                    SUPPLIERS
                </h3>
                <Form className="d-flex justify-content-center mt-3" onSubmit={handleSearch}>
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
                            dispatch({ type: "SET_ALL", payload: allData });
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
                        {fields.map((f) => (
                            <th key={f.key} className={f.className}>{f.label}</th>
                        ))}
                        <th className="actions-col text-center">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {editing && !updateKey && (
                        <tr>
                            {fields.map((f) => (
                                <td key={f.key} className={f.className}>
                                    {f.key === "supplierId" ? (
                                        "-"
                                    ) : (
                                        <input
                                            type={["phone", "postalCode"].includes(f.key) ? "tel" : "text"}
                                            value={editing[f.key] || ""}
                                            placeholder={
                                                f.key === "phone"
                                                    ? "0xxx-xxx-xx-xx"
                                                    : f.key === "postalCode"
                                                        ? "12345"
                                                        : ""
                                            }
                                            onChange={(e) => handleChange(f.key, e.target.value)}
                                            style={{ width: '100%' }}
                                        />
                                    )}
                                </td>
                            ))}
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

                    {suppliers.map((supplier) => {
                        const isEditing = updateKey === supplier.supplierId;
                        return (
                            <tr key={supplier.supplierId}>
                                {fields.map((f) => (
                                    <td key={f.key} className={f.className}>
                                        {isEditing && f.key !== "supplierId" ? (
                                            <input
                                                type={["phone", "postalCode"].includes(f.key) ? "tel" : "text"}
                                                value={editing[f.key] || ""}
                                                onChange={(e) => handleChange(f.key, e.target.value)}
                                                style={{ width: '100%' }}
                                            />
                                        ) : f.key === "phone" ? (
                                            formatPhone(supplier[f.key])
                                        ) : (
                                            supplier[f.key]
                                        )}
                                    </td>
                                ))}
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
                                                onClick={() => handleEdit(supplier)}
                                            >
                                                <FontAwesomeIcon icon={faArrowsRotate} />
                                            </Button>
                                            <Button
                                                variant="danger"
                                                size="sm"
                                                className="btn-compact"
                                                onClick={() => handleDelete(supplier.supplierId)}
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