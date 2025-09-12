import React, { useEffect, useReducer, useState } from "react";
import { getEmployees, addEmployee, deleteEmployee, getEmployeeById, updateEmployee } from "../services/EmployeeService";
import { Button, Table, Form } from "react-bootstrap";

const initialState = [];

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return action.payload;
        case "UPDATE_FIELD":
            return state.map(employee =>
                employee.employeeId === action.employeeId
                    ? { ...employee, [action.field]: action.value }
                    : employee
            );
        case "ADD_NEW":
            return [action.payload, ...state];
        case "REMOVE_NEW":
            return state.filter(employee => employee.employeeId !== action.employeeId);
        default:
            return state;
    }
}

export default function EmployeeList() {
    const [employees, dispatch] = useReducer(reducer, initialState);
    const [newEmployeeId, setNewEmployeeId] = useState(null);
    const [searchId, setSearchId] = useState("");
    const [updateId, setUpdateId] = useState(null);

    const loadEmployees = async () => {
        try {
            const response = await getEmployees();
            dispatch({ type: "SET_ALL", payload: response.data });
        } catch (error) {
            alert(error.message);
        }
    };

    useEffect(() => {
        loadEmployees();
    }, []);

    const handleChange = (employeeId, field, value) => {
        dispatch({ type: "UPDATE_FIELD", employeeId, field, value });
    };

    const handleAdd = () => {
        const tempId = "new-" + Math.random();
        const newEmployee = {
            employeeId: tempId,
            lastName: "",
            firstName: "",
            title: "",
            titleOfCourtesy: "",
            birthDate: "",
            hireDate: "",
            address: "",
            city: "",
            region: "",
            postalCode: "",
            country: "",
            homePhone: "",
            extension: "",
            photo: "",
            notes: "",
            reportsTo: "",
            photoPath: ""
        };
        dispatch({ type: "ADD_NEW", payload: newEmployee });
        setNewEmployeeId(tempId);
    };

    const handleCancel = () => {
        if (newEmployeeId) {
            dispatch({ type: "REMOVE_NEW", employeeId: newEmployeeId });
            setNewEmployeeId(null);
        }
    };

    const handleUpdate = (id) => setUpdateId(id);

    const handleSave = async (employee) => {
        try {
            if (employee.employeeId === newEmployeeId) {
                await addEmployee(employee);
                setNewEmployeeId(null);
            } else {
                await updateEmployee(employee);
                setUpdateId(null);
            }
            loadEmployees();
        } catch (error) {
            alert(error.message);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this employee?")) {
            try {
                await deleteEmployee(id);
                loadEmployees();
            } catch (error) {
                alert(error.message);
            }
        }
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!searchId) {
            loadEmployees();
            return;
        }
        try {
            const response = await getEmployeeById(searchId);
            dispatch({ type: "SET_ALL", payload: response.data ? [response.data] : [] });
        } catch (error) {
            alert(error.message);
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3>Employees</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
                <Form.Control
                    type="text"
                    placeholder="Search by ID"
                    value={searchId}
                    onChange={(e) => setSearchId(e.target.value)}
                    style={{ maxWidth: "200px", marginRight: "10px" }}
                />
                <Button type="submit" variant="info">Search</Button>
                <Button
                    variant="secondary"
                    className="ms-2"
                    onClick={() => { setSearchId(""); loadEmployees(); }}
                >
                    Reset
                </Button>
            </Form>

            <Button variant="success" className="mb-3" onClick={handleAdd}>
                + Add Employee
            </Button>

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Last Name</th>
                    <th>First Name</th>
                    <th>Title</th>
                    <th>Courtesy</th>
                    <th>Birth Date</th>
                    <th>Hire Date</th>
                    <th>Address</th>
                    <th>City</th>
                    <th>Region</th>
                    <th>Postal Code</th>
                    <th>Country</th>
                    <th>Phone</th>
                    <th>Extension</th>
                    <th>Photo</th>
                    <th>Notes</th>
                    <th>Reports To</th>
                    <th>Photo Path</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {employees.map(employee => {
                    const isEditing = updateId === employee.employeeId || newEmployeeId === employee.employeeId;
                    return (
                        <tr key={employee.employeeId}>
                            <td>
                                {employee.employeeId != null
                                    ? employee.employeeId.toString().startsWith("new-") ? "-" : employee.employeeId
                                    : "-"}
                            </td>
                            <td>{isEditing ? <input value={employee.lastName} onChange={(e) => handleChange(employee.employeeId, "lastName", e.target.value)} /> : employee.lastName}</td>
                            <td>{isEditing ? <input value={employee.firstName} onChange={(e) => handleChange(employee.employeeId, "firstName", e.target.value)} /> : employee.firstName}</td>
                            <td>{isEditing ? <input value={employee.title} onChange={(e) => handleChange(employee.employeeId, "title", e.target.value)} /> : employee.title}</td>
                            <td>{isEditing ? <input value={employee.titleOfCourtesy} onChange={(e) => handleChange(employee.employeeId, "titleOfCourtesy", e.target.value)} /> : employee.titleOfCourtesy}</td>
                            <td>{isEditing ? <input value={employee.birthDate} onChange={(e) => handleChange(employee.employeeId, "birthDate", e.target.value)} /> : employee.birthDate}</td>
                            <td>{isEditing ? <input value={employee.hireDate} onChange={(e) => handleChange(employee.employeeId, "hireDate", e.target.value)} /> : employee.hireDate}</td>
                            <td>{isEditing ? <input value={employee.address} onChange={(e) => handleChange(employee.employeeId, "address", e.target.value)} /> : employee.address}</td>
                            <td>{isEditing ? <input value={employee.city} onChange={(e) => handleChange(employee.employeeId, "city", e.target.value)} /> : employee.city}</td>
                            <td>{isEditing ? <input value={employee.region} onChange={(e) => handleChange(employee.employeeId, "region", e.target.value)} /> : employee.region}</td>
                            <td>{isEditing ? <input value={employee.postalCode} onChange={(e) => handleChange(employee.employeeId, "postalCode", e.target.value)} /> : employee.postalCode}</td>
                            <td>{isEditing ? <input value={employee.country} onChange={(e) => handleChange(employee.employeeId, "country", e.target.value)} /> : employee.country}</td>
                            <td>{isEditing ? <input value={employee.homePhone} onChange={(e) => handleChange(employee.employeeId, "homePhone", e.target.value)} /> : employee.homePhone}</td>
                            <td>{isEditing ? <input value={employee.extension} onChange={(e) => handleChange(employee.employeeId, "extension", e.target.value)} /> : employee.extension}</td>
                            <td>{isEditing ? <input value={employee.photo} onChange={(e) => handleChange(employee.employeeId, "photo", e.target.value)} /> : employee.photo}</td>
                            <td>{isEditing ? <input value={employee.notes} onChange={(e) => handleChange(employee.employeeId, "notes", e.target.value)} /> : employee.notes}</td>
                            <td>{isEditing ? <input value={employee.reportsTo} onChange={(e) => handleChange(employee.employeeId, "reportsTo", e.target.value)} /> : employee.reportsTo}</td>
                            <td>{isEditing ? <input value={employee.photoPath} onChange={(e) => handleChange(employee.employeeId, "photoPath", e.target.value)} /> : employee.photoPath}</td>
                            <td>
                                {isEditing ? (
                                    <>
                                        <Button variant="primary" size="sm" onClick={() => handleSave(employee)}>Save</Button>
                                        {employee.employeeId === newEmployeeId && <Button variant="secondary" size="sm" className="ms-2" onClick={handleCancel}>Cancel</Button>}
                                    </>
                                ) : (
                                    <>
                                        <Button variant="warning" size="sm" className="me-2" onClick={() => handleUpdate(employee.employeeId)}>Update</Button>
                                        <Button variant="danger" size="sm" onClick={() => handleDelete(employee.employeeId)}>Delete</Button>
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
