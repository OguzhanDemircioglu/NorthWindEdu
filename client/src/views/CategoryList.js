import React, { useEffect, useReducer, useState } from "react";
import { getCategories, addCategory, deleteCategory, getCategoryById, updateCategory } from "../services/CategoryService";
import { Button, Table, Form } from "react-bootstrap";

const initialState = [];

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload;
        case "UPDATE_FIELD":
            return state.map(category =>
                category.categoryId === action.categoryId
                    ? { ...category, [action.field]: action.value }
                    : category
            );
        case "ADD_NEW":
            return [action.payload, ...state];
        case "REMOVE_NEW":
            return state.filter(category => category.categoryId !== action.categoryId);
        default:
            return state;
    }
}

export default function CategoryList() {
    const [categories, dispatch] = useReducer(reducer, initialState);
    const [newCategoryId, setNewCategoryId] = useState(null);
    const [searchId, setSearchId] = useState("");
    const [updateId, setUpdateId] = useState(null);

    const loadCategories = async () => {
        try {
            const response = await getCategories();
            dispatch({ type: "SET_ALL", payload: response.data });
        } catch (error) {
            alert(error.message);
        }
    };

    useEffect(() => {
        loadCategories();
    }, []);

    const handleChange = (categoryId, field, value) => {
        dispatch({ type: "UPDATE_FIELD", categoryId, field, value });
    };

    const handleAdd = () => {
        const tempId = "new-" + Math.random();
        const newCategory = { categoryId: tempId, categoryName: "", description: "", picture: "" };
        dispatch({ type: "ADD_NEW", payload: newCategory });
        setNewCategoryId(tempId);
    };

    const handleCancel = () => {
        if (newCategoryId) {
            dispatch({ type: "REMOVE_NEW", categoryId: newCategoryId });
            setNewCategoryId(null);
        }
    };

    const handleUpdate = (id) => {
        setUpdateId(id);
    };

    const handleSave = async (category) => {
        try {
            if (category.categoryId === newCategoryId) {
                await addCategory(category);
                setNewCategoryId(null);
            } else {
                await updateCategory(category);
                setUpdateId(null);
            }
            loadCategories();
        } catch (error) {
            alert(error.message);
        }
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

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!searchId) {
            loadCategories();
            return;
        }
        try {
            const response = await getCategoryById(searchId);
            dispatch({ type: "SET_ALL", payload: response.data ? [response.data] : [] });
        } catch (error) {
            alert(error.message);
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3>Categories</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
                <Form.Control
                    type="number"
                    placeholder="Search by ID"
                    value={searchId}
                    onChange={(e) => setSearchId(e.target.value)}
                    style={{ maxWidth: "200px", marginRight: "10px" }}
                />
                <Button type="submit" variant="info">Search</Button>
                <Button
                    variant="secondary"
                    className="ms-2"
                    onClick={() => {
                        setSearchId("");
                        loadCategories();
                    }}
                >
                    Reset
                </Button>
            </Form>

            <Button variant="success" className="mb-3" onClick={handleAdd}>
                + Add Category
            </Button>

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Picture</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {categories.map(category => {
                    const isEditing = updateId === category.categoryId || newCategoryId === category.categoryId;
                    return (
                        <tr key={category.categoryId}>
                            <td>{category.categoryId.toString().startsWith("new-") ? "-" : category.categoryId}</td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={category.categoryName}
                                        onChange={(e) => handleChange(category.categoryId, "categoryName", e.target.value)}
                                    />
                                ) : category.categoryName}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={category.description}
                                        onChange={(e) => handleChange(category.categoryId, "description", e.target.value)}
                                    />
                                ) : category.description}
                            </td>
                            <td>
                                {isEditing ? (
                                    <input
                                        value={category.picture}
                                        onChange={(e) => handleChange(category.categoryId, "picture", e.target.value)}
                                    />
                                ) : category.picture ? (
                                    <img src={category.picture} alt="category" width="50" />
                                ) : "-"}
                            </td>
                            <td>
                                {isEditing ? (
                                    <>
                                        <Button variant="primary" size="sm" onClick={() => handleSave(category)}>
                                            Save
                                        </Button>
                                        {category.categoryId === newCategoryId && (
                                            <Button variant="secondary" size="sm" className="ms-2" onClick={handleCancel}>
                                                Cancel
                                            </Button>
                                        )}
                                    </>
                                ) : (
                                    <>
                                        <Button variant="warning" size="sm" className="me-2" onClick={() => handleUpdate(category.categoryId)}>
                                            Update
                                        </Button>
                                        <Button variant="danger" size="sm" onClick={() => handleDelete(category.categoryId)}>
                                            Delete
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