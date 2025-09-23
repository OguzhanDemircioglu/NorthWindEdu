import React, { useEffect, useReducer, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAdd, faArrowsRotate, faSave, faTrash, faCancel, faSearch, faRotateRight } from "@fortawesome/free-solid-svg-icons";
import { getAllProducts, addProduct, updateProduct, deleteProduct } from "../services/ProductService";

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
    const [editing, setEditing] = useState(null);
    const [updateKey, setUpdateKey] = useState(null);
    const [allData, setAllData] = useState([]);
    const [searchText, setSearchText] = useState("");
    const [searchColumn, setSearchColumn] = useState("productId");

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

    useEffect(() => { loadData(); }, []);

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
        const filtered = allData.filter((p) => {
            const value = p[searchColumn];
            if (value === null || value === undefined) return false;
            return value.toString().toLowerCase().includes(searchText.toLowerCase());
        });
        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3>Products</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
                <Form.Select
                    style={{ maxWidth: "150px", marginRight: "10px" }}
                    value={searchColumn}
                    onChange={(e) => setSearchColumn(e.target.value)}
                >
                    <option value="productId">Product ID</option>
                    <option value="productName">Name</option>
                    <option value="supplierId">Supplier ID</option>
                    <option value="categoryId">Category ID</option>
                    <option value="quantityPerUnit">Quantity</option>
                    <option value="unitPrice">Price</option>
                    <option value="unitsInStock">Stock</option>
                    <option value="reorderLevel">Reorder Level</option>
                    <option value="discontinued">Discontinued</option>
                </Form.Select>

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
            </Form>

            <Button variant="success" className="mb-3" onClick={handleAdd}>
                <FontAwesomeIcon icon={faAdd} />
            </Button>

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Supplier ID</th>
                    <th>Category ID</th>
                    <th>Quantity per Unit</th>
                    <th>Unit Price</th>
                    <th>Units In Stock</th>
                    <th>Units In Order</th>
                    <th>Reorder Level</th>
                    <th>Discontinued</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {editing && !updateKey && (
                    <tr>
                        <td>-</td>
                        <td><input value={editing.productName} onChange={e => setEditing({...editing, productName: e.target.value})}/></td>
                        <td><input value={editing.supplierId} onChange={e => setEditing({...editing, supplierId: e.target.value})}/></td>
                        <td><input value={editing.categoryId} onChange={e => setEditing({...editing, categoryId: e.target.value})}/></td>
                        <td><input value={editing.quantityPerUnit} onChange={e => setEditing({...editing, quantityPerUnit: e.target.value})}/></td>
                        <td><input value={editing.unitPrice} onChange={e => setEditing({...editing, unitPrice: e.target.value})}/></td>
                        <td><input value={editing.unitsInStock} onChange={e => setEditing({...editing, unitsInStock: e.target.value})}/></td>
                        <td><input value={editing.unitsInOrder} onChange={e => setEditing({...editing, unitsInOrder: e.target.value})}/></td>
                        <td><input value={editing.reorderLevel} onChange={e => setEditing({...editing, reorderLevel: e.target.value})}/></td>
                        <td>
                            <Form.Select value={editing.discontinued} onChange={e => setEditing({...editing, discontinued: Number(e.target.value)})}>
                                <option value={0}>0</option>
                                <option value={1}>1</option>
                            </Form.Select>
                        </td>
                        <td>
                            <Button variant="primary" size="sm" onClick={() => handleSave(editing)}><FontAwesomeIcon icon={faSave} /></Button>
                            <Button variant="secondary" size="sm" className="ms-2" onClick={handleCancel}><FontAwesomeIcon icon={faCancel} /></Button>
                        </td>
                    </tr>
                )}

                {products.map((d, i) => {
                    const isEditing = updateKey === d.productId;
                    return (
                        <tr key={i}>
                            <td>{d.productId}</td>
                            <td>{isEditing ? <input value={editing.productName} onChange={e => setEditing({...editing, productName: e.target.value})}/> : d.productName}</td>
                            <td>{isEditing ? <input value={editing.supplierId} onChange={e => setEditing({...editing, supplierId: e.target.value})}/> : d.supplierId}</td>
                            <td>{isEditing ? <input value={editing.categoryId} onChange={e => setEditing({...editing, categoryId: e.target.value})}/> : d.categoryId}</td>
                            <td>{isEditing ? <input value={editing.quantityPerUnit} onChange={e => setEditing({...editing, quantityPerUnit: e.target.value})}/> : d.quantityPerUnit}</td>
                            <td>{isEditing ? <input value={editing.unitPrice} onChange={e => setEditing({...editing, unitPrice: e.target.value})}/> : d.unitPrice}</td>
                            <td>{isEditing ? <input value={editing.unitsInStock} onChange={e => setEditing({...editing, unitsInStock: e.target.value})}/> : d.unitsInStock}</td>
                            <td>{isEditing ? <input value={editing.unitsInOrder} onChange={e => setEditing({...editing, unitsInOrder: e.target.value})}/> : d.unitsInOrder}</td>
                            <td>{isEditing ? <input value={editing.reorderLevel} onChange={e => setEditing({...editing, reorderLevel: e.target.value})}/> : d.reorderLevel}</td>
                            <td>
                                {isEditing ? (
                                    <Form.Select value={editing.discontinued} onChange={e=>setEditing({...editing, discontinued: Number(e.target.value)})}>
                                        <option value={0}>0</option>
                                        <option value={1}>1</option>
                                    </Form.Select>
                                ) : (
                                    d.discontinued
                                )}
                            </td>
                            <td>
                                {isEditing ? (
                                    <>
                                        <Button variant="primary" size="sm" onClick={() => handleSave(editing)}><FontAwesomeIcon icon={faSave} /></Button>
                                        <Button variant="secondary" size="sm" className="ms-2" onClick={handleCancel}><FontAwesomeIcon icon={faCancel} /></Button>
                                    </>
                                ) : (
                                    <>
                                        <Button variant="warning" size="sm" className="me-2" onClick={() => handleEdit(d)}><FontAwesomeIcon icon={faArrowsRotate} /></Button>
                                        <Button variant="danger" size="sm" onClick={() => handleDelete(d.productId)}><FontAwesomeIcon icon={faTrash} /></Button>
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