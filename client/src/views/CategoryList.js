import React, { useEffect, useReducer, useState } from "react";
import {getCategories, addCategory, deleteCategory, updateCategory,} from "../services/CategoryService";
import { Button, Table, Form } from "react-bootstrap";
import {faAdd, faArrowsRotate, faCancel, faRotateRight, faSave, faSearch, faTrash,} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const initialState = [];

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload;
        default:
            return state;
    }
}

export default function CategoryList() {
    const [categories, dispatch] = useReducer(reducer, initialState);
    const [updateId, setUpdateId] = useState(null);
    const [editingCategory, setEditingCategory] = useState(null);
    const [searchColumn, setSearchColumn] = useState(null);
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
            cat[searchColumn]
                ?.toString()
                .toLowerCase()
                .includes(searchText.toLowerCase())
        );

        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3>Categories</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
                <Form.Select
                    value={searchColumn}
                    onChange={(e) => setSearchColumn(e.target.value)}
                    style={{ maxWidth: "150px", marginRight: "10px" }}
                >
                    <option value="categoryId">ID</option>
                    <option value="categoryName">Name</option>
                    <option value="description">Description</option>
                </Form.Select>

                <Form.Control
                    type="text"
                    placeholder={`Search`}
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
                        dispatch({ type: "SET_ALL", payload: allCategories });
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
                    <th>Description</th>
                    <th>Actions</th>
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
                                size="sm"
                                onClick={() => handleSave(editingCategory)}
                            >
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

                {categories.map((category) => (
                    <tr key={category.categoryId}>
                        <td>{category.categoryId}</td>
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
                        <td>
                            {updateId === category.categoryId ? (
                                <>
                                    <Button
                                        variant="primary"
                                        size="sm"
                                        onClick={() => handleSave(editingCategory)}
                                    >
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
                                        onClick={() => handleUpdate(category)}
                                    >
                                        <FontAwesomeIcon icon={faArrowsRotate} />
                                    </Button>
                                    <Button
                                        variant="danger"
                                        size="sm"
                                        onClick={() => handleDelete(category.categoryId)}
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
    );
}
