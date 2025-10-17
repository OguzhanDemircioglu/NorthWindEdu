import React, { useEffect, useReducer, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faAdd, faArrowsRotate, faSave, faTrash, faCancel, faSearch, faRotateRight} from "@fortawesome/free-solid-svg-icons";
import {getAllProducts, addProduct, updateProduct, deleteProduct} from "../services/ProductService";
import { getAllSuppliers } from "../services/SupplierService";
import { getCategories } from "../services/CategoryService";

const initialState = [];


function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload || [];
        default:
            return state;
    }
}

export default function ProductList() {
    const [products, dispatch] = useReducer(reducer, initialState);
    const [suppliers, setSuppliers] = useState([]);
    const [categories, setCategories] = useState([]);
    const [editing, setEditing] = useState(null);
    const [updateKey, setUpdateKey] = useState(null);
    const [allData, setAllData] = useState([]);
    const [searchText, setSearchText] = useState("");

    const loadData = async () => {
        try {
            const res = await getAllProducts();
            setAllData(res.data || []);
            dispatch({ type: "SET_ALL", payload: res.data || [] });
        } catch (e) {
            setAllData([]);
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    const loadLookups = async () => {
        try {
            const [supplier, category] = await Promise.all([
                getAllSuppliers(),
                getCategories(),
            ]);
            setSuppliers(supplier.data || []);
            setCategories(category.data || []);
        } catch (e) {
            console.error("Error loading data:", e);
            setSuppliers([]);
            setCategories([]);
        }
    };

    useEffect(() => {
        loadData();
        loadLookups();
    }, []);

    const handleAdd = () => {
        if (editing) return;
        setEditing({
            productName: "",
            supplierId: "",
            categoryId: "",
            quantityPerUnit: "",
            unitPrice: "",
            unitsInStock: 0,
            unitsInOrder: 0,
            reorderLevel: 0,
            discontinued: 0
        });
        setUpdateKey(null);
    };

    const handleEdit = (item) => {
        setEditing({ ...item });
        setUpdateKey(item.productId);
    };

    const handleSave = async (item) => {
        try {
            if (!updateKey) {
                await addProduct(item);
            } else {
                await updateProduct(item);
            }
            setEditing(null);
            setUpdateKey(null);
            loadData();
        } catch (e) {
            console.error("Save failed", e);
            alert("Save failed: " + e.message);
        }
    };

    const handleCancel = () => {
        setEditing(null);
        setUpdateKey(null);
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this product?")) {
            try {
                await deleteProduct(id);
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
        const filtered = allData.filter((p) =>
            Object.values(p).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );
        dispatch({ type: "SET_ALL", payload: filtered });
    };

    const getSupplierName = (id) =>
        suppliers.find((s) => s.supplierId === id)?.companyName || "";

    const getCategoryName = (id) =>
        categories.find((c) => c.categoryId === id)?.categoryName || "";

    return (
        <div style={{ padding: "20px" }}>
            <div style={{ textAlign: "center", marginBottom: "20px" }}>
                <h3 style={{color: '#343a40', fontWeight: '600', paddingBottom: '5px', borderBottom: '3px solid #6c757d', textTransform: 'uppercase', letterSpacing: '1.5px', marginBottom: '15px'}}>
                    PRODUCTS
                </h3>
                <Form className="d-flex justify-content-center mt-3" onSubmit={handleSearch}>
                    <Form.Control
                        type="text"
                        placeholder="Search"
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
                        <th>Name</th>
                        <th>Supplier</th>
                        <th>Category</th>
                        <th>Quantity per Unit</th>
                        <th>Unit Price</th>
                        <th>Units In Stock</th>
                        <th>Units In Order</th>
                        <th>Reorder Level</th>
                        <th>Discontinued</th>
                        <th className="actions-col" style={{ width: '80px' }}>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {editing && !updateKey && (
                        <tr>
                            <td className="id-col">-</td>
                            <td>
                                <input
                                    value={editing.productName}
                                    onChange={(e) =>
                                        setEditing({ ...editing, productName: e.target.value })
                                    }
                                    style={{ width: '100%' }}
                                />
                            </td>
                            <td>
                                <Form.Select
                                    value={editing.supplierId || ""}
                                    onChange={(e) =>
                                        setEditing({ ...editing, supplierId: Number(e.target.value) })
                                    }
                                    style={{ width: '100%', minWidth: '100px' }}
                                >
                                    <option value="">Select</option>
                                    {suppliers.map((s) => (
                                        <option key={s.supplierId} value={s.supplierId}>
                                            {s.companyName}
                                        </option>
                                    ))}
                                </Form.Select>
                            </td>
                            <td>
                                <Form.Select
                                    value={editing.categoryId || ""}
                                    onChange={(e) =>
                                        setEditing({ ...editing, categoryId: Number(e.target.value) })
                                    }
                                    style={{ width: '100%', minWidth: '80px' }}
                                >
                                    <option value="">Select</option>
                                    {categories.map((c) => (
                                        <option key={c.categoryId} value={c.categoryId}>
                                            {c.categoryName}
                                        </option>
                                    ))}
                                </Form.Select>
                            </td>
                            <td>
                                <input
                                    value={editing.quantityPerUnit}
                                    onChange={(e) =>
                                        setEditing({ ...editing, quantityPerUnit: e.target.value })
                                    }
                                    style={{ width: '100%' }}
                                />
                            </td>
                            <td>
                                <input
                                    type="number"
                                    value={editing.unitPrice}
                                    onChange={(e) =>
                                        setEditing({ ...editing, unitPrice: e.target.value })
                                    }
                                    style={{ width: '100%' }}
                                />
                            </td>
                            <td>
                                <input
                                    type="number"
                                    value={editing.unitsInStock}
                                    onChange={(e) =>
                                        setEditing({ ...editing, unitsInStock: e.target.value })
                                    }
                                    style={{ width: '100%' }}
                                />
                            </td>
                            <td>
                                <input
                                    type="number"
                                    value={editing.unitsInOrder}
                                    onChange={(e) =>
                                        setEditing({ ...editing, unitsInOrder: e.target.value })
                                    }
                                    style={{ width: '100%' }}
                                />
                            </td>
                            <td>
                                <input
                                    type="number"
                                    value={editing.reorderLevel}
                                    onChange={(e) =>
                                        setEditing({ ...editing, reorderLevel: e.target.value })
                                    }
                                    style={{ width: '100%' }}
                                />
                            </td>
                            <td>
                                <Form.Select
                                    value={editing.discontinued}
                                    onChange={(e) =>
                                        setEditing({ ...editing, discontinued: Number(e.target.value) })
                                    }
                                >
                                    <option value={0}>0</option>
                                    <option value={1}>1</option>
                                </Form.Select>
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

                    {products.map((d, index) => {
                        const isEditing = updateKey === d.productId;
                        return (
                            <tr key={d.productId}>
                                <td className="id-col">{index + 1}</td>
                                <td>
                                    {isEditing ? (
                                        <input
                                            value={editing.productName}
                                            onChange={(e) =>
                                                setEditing({ ...editing, productName: e.target.value })
                                            }
                                            style={{ width: '100%' }}
                                        />
                                    ) : (
                                        d.productName
                                    )}
                                </td>
                                <td>
                                    {isEditing ? (
                                        <Form.Select
                                            value={editing.supplierId || ""}
                                            onChange={(e) =>
                                                setEditing({
                                                    ...editing,
                                                    supplierId: Number(e.target.value),
                                                })
                                            }
                                            style={{ width: '100%', minWidth: '100px' }}
                                        >
                                            <option value="">Select supplier</option>
                                            {suppliers.map((s) => (
                                                <option key={s.supplierId} value={s.supplierId}>
                                                    {s.companyName}
                                                </option>
                                            ))}
                                        </Form.Select>
                                    ) : (
                                        getSupplierName(d.supplierId)
                                    )}
                                </td>
                                <td>
                                    {isEditing ? (
                                        <Form.Select
                                            value={editing.categoryId || ""}
                                            onChange={(e) =>
                                                setEditing({
                                                    ...editing,
                                                    categoryId: Number(e.target.value),
                                                })
                                            }
                                            style={{ width: '100%', minWidth: '80px' }}
                                        >
                                            <option value="">Select category</option>
                                            {categories.map((c) => (
                                                <option key={c.categoryId} value={c.categoryId}>
                                                    {c.categoryName}
                                                </option>
                                            ))}
                                        </Form.Select>
                                    ) : (
                                        getCategoryName(d.categoryId)
                                    )}
                                </td>
                                <td>
                                    {isEditing ? (
                                        <input
                                            value={editing.quantityPerUnit}
                                            onChange={(e) =>
                                                setEditing({
                                                    ...editing,
                                                    quantityPerUnit: e.target.value,
                                                })
                                            }
                                            style={{ width: '100%' }}
                                        />
                                    ) : (
                                        d.quantityPerUnit
                                    )}
                                </td>
                                <td>
                                    {isEditing ? (
                                        <input
                                            type="number"
                                            value={editing.unitPrice}
                                            onChange={(e) =>
                                                setEditing({ ...editing, unitPrice: e.target.value })
                                            }
                                            style={{ width: '100%' }}
                                        />
                                    ) : (
                                        d.unitPrice
                                    )}
                                </td>
                                <td>
                                    {isEditing ? (
                                        <input
                                            type="number"
                                            value={editing.unitsInStock}
                                            onChange={(e) =>
                                                setEditing({ ...editing, unitsInStock: e.target.value })
                                            }
                                            style={{ width: '100%' }}
                                        />
                                    ) : (
                                        d.unitsInStock
                                    )}
                                </td>
                                <td>
                                    {isEditing ? (
                                        <input
                                            type="number"
                                            value={editing.unitsInOrder}
                                            onChange={(e) =>
                                                setEditing({ ...editing, unitsInOrder: e.target.value })
                                            }
                                            style={{ width: '100%' }}
                                        />
                                    ) : (
                                        d.unitsInOrder
                                    )}
                                </td>
                                <td>
                                    {isEditing ? (
                                        <input
                                            type="number"
                                            value={editing.reorderLevel}
                                            onChange={(e) =>
                                                setEditing({
                                                    ...editing,
                                                    reorderLevel: e.target.value,
                                                })
                                            }
                                            style={{ width: '100%' }}
                                        />
                                    ) : (
                                        d.reorderLevel
                                    )}
                                </td>
                                <td>
                                    {isEditing ? (
                                        <Form.Select
                                            value={editing.discontinued}
                                            onChange={(e) =>
                                                setEditing({
                                                    ...editing,
                                                    discontinued: Number(e.target.value),
                                                })
                                            }
                                        >
                                            <option value={0}>0</option>
                                            <option value={1}>1</option>
                                        </Form.Select>
                                    ) : (
                                        d.discontinued
                                    )}
                                </td>
                                <td className="text-center">
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
                                                onClick={() => handleEdit(d)}
                                                title="Update"
                                            >
                                                <FontAwesomeIcon icon={faArrowsRotate} />
                                            </Button>
                                            <Button
                                                variant="danger"
                                                size="sm"
                                                className="btn-compact"
                                                onClick={() => handleDelete(d.productId)}
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