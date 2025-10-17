import React, { useEffect, useReducer, useState } from "react";
import {getCategories, addCategory, deleteCategory, updateCategory,} from "../services/CategoryService";
import { Button, Table, Form } from "react-bootstrap";
import {faAdd, faArrowsRotate, faCancel, faRotateRight, faSave, faSearch, faTrash,} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const initialState = [];
function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload || [];
        default:
            return state;
    }
}

export default function CategoryList() {
    const [categories, dispatch] = useReducer(reducer, initialState);
    const [updateId, setUpdateId] = useState(null);
    const [editingCategory, setEditingCategory] = useState(null);
    const [searchText, setSearchText] = useState("");
    const [allCategories, setAllCategories] = useState([]);

    const allowedFields = ["categoryId", "categoryName", "description"];

    const loadCategories = async () => {
        try {
            const response = await getCategories();
            const cleanedCategories = response.data.map((c) => {
                const filtered = {};
                allowedFields.forEach((f) => {
                    if (c.hasOwnProperty(f)) filtered[f] = c[f];
                });
                return filtered;
            });

            setAllCategories(cleanedCategories);
            dispatch({ type: "SET_ALL", payload: cleanedCategories });
        } catch (error) {
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    useEffect(() => {
        loadCategories();
    }, []);

    const handleChange = (field, value) => {
        setEditingCategory((prev) => ({ ...prev, [field]: value }));
    };

    const handleAdd = () => {
        if (editingCategory) return;

        setEditingCategory({
            categoryName: "",
            description: "",
        });
        setUpdateId(null);
    };

    const handleUpdate = (category) => {
        const cleanedCategory = {};
        allowedFields.forEach((f) => {
            if (category.hasOwnProperty(f)) cleanedCategory[f] = category[f];
        });
        setEditingCategory(cleanedCategory);
        setUpdateId(category.categoryId);
    };

    const handleSave = async (category) => {
        try {
            if (!updateId) {
                const { categoryId, ...newCategory } = category;
                await addCategory(newCategory);
            } else {
                await updateCategory(category);
            }
            setEditingCategory(null);
            setUpdateId(null);
            loadCategories();
        } catch (error) {
            alert(error.message);
        }
    };

    const handleCancel = () => {
        setEditingCategory(null);
        setUpdateId(null);
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this category?")) {
            try {
                await deleteCategory(id);
                loadCategories();
            } catch (error) {
                alert(error.message);
            }
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        if (!searchText) {
            dispatch({ type: "SET_ALL", payload: allCategories });
            return;
        }

        const filtered = allCategories.filter((cat) =>
            Object.values(cat).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );

        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <div style={{ textAlign: "center", marginBottom: "20px" }}>
                <h3 style={{color: '#343a40', fontWeight: '600', paddingBottom: '5px', borderBottom: '3px solid #6c757d', textTransform: 'uppercase', letterSpacing: '1.5px', marginBottom: '15px'}}>
                    CATEGORIES
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
                            dispatch({ type: "SET_ALL", payload: allCategories });
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


            <div className="table-wrapper">
                <Table striped bordered hover className="table-compact">
                    <thead>
                    <tr>
                        <th className="id-col">-</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th className="actions-col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {editingCategory && !updateId && (
                        <tr>
                            <td>-</td>
                            <td>
                                <input
                                    value={editingCategory.categoryName}
                                    onChange={(e) => handleChange("categoryName", e.target.value)}
                                />
                            </td>
                            <td>
                                <input
                                    value={editingCategory.description}
                                    onChange={(e) => handleChange("description", e.target.value)}
                                />
                            </td>
                            <td>
                                <Button
                                    variant="primary"
                                    className="btn-compact"
                                    onClick={() => handleSave(editingCategory)}
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

                    {categories.map((category, index) => (
                        <tr key={category.categoryId}>
                            <td className="id-col">{index + 1}</td>
                            <td>
                                {updateId === category.categoryId ? (
                                    <input
                                        value={editingCategory.categoryName}
                                        onChange={(e) => handleChange("categoryName", e.target.value)}
                                    />
                                ) : (
                                    category.categoryName
                                )}
                            </td>
                            <td>
                                {updateId === category.categoryId ? (
                                    <input
                                        value={editingCategory.description}
                                        onChange={(e) => handleChange("description", e.target.value)}
                                    />
                                ) : (
                                    category.description
                                )}
                            </td>
                            <td className="actions-col">
                                {updateId === category.categoryId ? (
                                    <>
                                        <Button
                                            variant="primary"
                                            className="btn-compact me-2"
                                            onClick={() => handleSave(editingCategory)}
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
                                            onClick={() => handleUpdate(category)}
                                            title="Update"
                                        >
                                            <FontAwesomeIcon icon={faArrowsRotate} />
                                        </Button>
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            className="btn-compact"
                                            onClick={() => handleDelete(category.categoryId)}
                                            title="Delete"
                                        >
                                            <FontAwesomeIcon icon={faTrash} />
                                        </Button>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            </div>
        </div>
    );
}