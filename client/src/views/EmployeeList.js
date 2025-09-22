import React, { useEffect, useReducer, useState } from "react";
import {
    getEmployees,
    addEmployee,
    deleteEmployee,
    updateEmployee,
} from "../services/EmployeeService";
import { Button, Table, Form } from "react-bootstrap";
import {
    faAdd,
    faArrowsRotate,
    faCancel,
    faRotateRight,
    faSave,
    faSearch,
    faTrash,
} from "@fortawesome/free-solid-svg-icons";
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

export default function EmployeeList() {
    const [employees, dispatch] = useReducer(reducer, initialState);
    const [updateId, setUpdateId] = useState(null);
    const [editingEmployee, setEditingEmployee] = useState(null);
    const [searchColumn, setSearchColumn] = useState(null);
    const [searchText, setSearchText] = useState("");
    const [allEmployees, setAllEmployees] = useState([]);

    const allowedFields = [
        "employeeId",
        "lastName",
        "firstName",
        "title",
        "titleOfCourtesy",
        "birthDate",
        "hireDate",
        "city",
        "region",
        "postalCode",
        "country",
        "homePhone",
    ];

    const loadEmployees = async () => {
        try {
            const response = await getEmployees();
            const cleanedEmployees = response.data.map((e) => {
                const filtered = {};
                allowedFields.forEach((f) => {
                    if (e.hasOwnProperty(f)) filtered[f] = e[f];
                });
                return filtered;
            });

            setAllEmployees(cleanedEmployees);
            dispatch({ type: "SET_ALL", payload: cleanedEmployees });
        } catch (error) {
            dispatch({ type: "SET_ALL", payload: [] });
        }
    };

    useEffect(() => {
        loadEmployees();
    }, []);

    const formatDate = (value) => {
        if (!value) return "";
        let digits = value.replace(/\D/g, "").slice(0, 8);
        const year = digits.slice(0, 4);
        const month = digits.slice(4, 6);
        const day = digits.slice(6, 8);

        let result = year;
        if (month) result += "-" + month;
        if (day) result += "-" + day;

        return result;
    };

    const formatPhone = (value) => {
        if (!value) return "";
        const digits = value.replace(/\D/g, "").slice(0, 11);
        const part1 = digits.slice(0, 4);
        const part2 = digits.slice(4, 7);
        const part3 = digits.slice(7, 9);
        const part4 = digits.slice(9, 11);

        let result = part1;
        if (part2) result += "-" + part2;
        if (part3) result += "-" + part3;
        if (part4) result += "-" + part4;

        return result;
    };

    const handleChange = (field, value) => {
        if (field === "homePhone") {
            setEditingEmployee((prev) => ({
                ...prev,
                [field]: formatPhone(value),
            }));
        } else if (field === "birthDate" || field === "hireDate") {
            setEditingEmployee((prev) => ({
                ...prev,
                [field]: formatDate(value),
            }));
        } else {
            setEditingEmployee((prev) => ({ ...prev, [field]: value }));
        }
    };

    const handleAdd = () => {
        if (editingEmployee) return;

        setEditingEmployee({
            lastName: "",
            firstName: "",
            title: "",
            titleOfCourtesy: "",
            birthDate: "",
            hireDate: "",
            city: "",
            region: "",
            postalCode: "",
            country: "",
            homePhone: "",
        });
        setUpdateId(null);
    };

    const handleUpdate = (employee) => {
        const cleanedEmployee = {};
        allowedFields.forEach((f) => {
            if (employee.hasOwnProperty(f)) cleanedEmployee[f] = employee[f];
        });
        setEditingEmployee(cleanedEmployee);
        setUpdateId(employee.employeeId);
    };

    const handleSave = async (employee) => {
        try {
            const cleaned = {
                ...employee,
                homePhone: employee.homePhone?.replace(/\D/g, ""),
            };

            if (!updateId) {
                const { employeeId, ...newEmployee } = cleaned;
                await addEmployee(newEmployee);
            } else {
                await updateEmployee(cleaned);
            }
            setEditingEmployee(null);
            setUpdateId(null);
            loadEmployees();
        } catch (error) {
            alert(error.message);
        }
    };

    const handleCancel = () => {
        setEditingEmployee(null);
        setUpdateId(null);
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

    const handleSearch = (e) => {
        e.preventDefault();
        if (!searchText) {
            dispatch({ type: "SET_ALL", payload: allEmployees });
            return;
        }

        const filtered = allEmployees.filter((emp) =>
            emp[searchColumn]
                ?.toString()
                .toLowerCase()
                .includes(searchText.toLowerCase())
        );

        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <h3>Employees</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
                <Form.Select
                    value={searchColumn}
                    onChange={(e) => setSearchColumn(e.target.value)}
                    style={{ maxWidth: "150px", marginRight: "10px" }}
                >
                    <option value="employeeId">ID</option>
                    <option value="lastName">Last Name</option>
                    <option value="firstName">First Name</option>
                    <option value="title">Title</option>
                    <option value="titleOfCourtesy">Courtesy</option>
                    <option value="city">City</option>
                    <option value="country">Country</option>
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
                        dispatch({ type: "SET_ALL", payload: allEmployees });
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
                    <th>Last Name</th>
                    <th>First Name</th>
                    <th>Title</th>
                    <th>Courtesy</th>
                    <th>Birth Date</th>
                    <th>Hire Date</th>
                    <th>City</th>
                    <th>Region</th>
                    <th>Postal Code</th>
                    <th>Country</th>
                    <th>Phone</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {editingEmployee && !updateId && (
                    <tr>
                        <td>{editingEmployee.employeeId || "-"}</td>

                        {allowedFields
                            .filter((field) => field !== "employeeId")
                            .map((field) => (
                                <td key={field}>
                                    {field === "titleOfCourtesy" ? (
                                        <Form.Select
                                            value={editingEmployee[field] || ""}
                                            onChange={(e) => handleChange(field, e.target.value)}
                                        >
                                            <option value="">Select</option>
                                            <option value="Mr.">Mr.</option>
                                            <option value="Mrs.">Mrs.</option>
                                            <option value="Ms.">Ms.</option>
                                            <option value="Dr.">Dr.</option>
                                        </Form.Select>
                                    ) : (
                                        <input
                                            type={field === "homePhone" ? "tel" : "text"}
                                            value={editingEmployee[field] || ""}
                                            placeholder={
                                                field === "homePhone"
                                                    ? "0xxx-xxx-xx-xx"
                                                    : field === "birthDate" || field === "hireDate"
                                                        ? "yyyy-mm-dd"
                                                        : ""
                                            }
                                            onChange={(e) => handleChange(field, e.target.value)}
                                        />
                                    )}
                                </td>
                            ))}

                        <td>
                            <Button
                                variant="primary"
                                size="sm"
                                onClick={() => handleSave(editingEmployee)}
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

                {employees.map((employee) => {
                    const isEditing = updateId === employee.employeeId;
                    return (
                        <tr key={employee.employeeId}>
                            <td>{employee.employeeId}</td>

                            {allowedFields
                                .filter((field) => field !== "employeeId")
                                .map((field) => (
                                    <td key={field}>
                                        {isEditing ? (
                                            field === "titleOfCourtesy" ? (
                                                <Form.Select
                                                    value={editingEmployee[field] || ""}
                                                    onChange={(e) => handleChange(field, e.target.value)}
                                                >
                                                    <option value="">Select</option>
                                                    <option value="Mr.">Mr.</option>
                                                    <option value="Mrs.">Mrs.</option>
                                                    <option value="Ms.">Ms.</option>
                                                    <option value="Dr.">Dr.</option>
                                                </Form.Select>
                                            ) : (
                                                <input
                                                    type={field === "homePhone" ? "tel" : "text"}
                                                    value={editingEmployee[field] || ""}
                                                    placeholder={
                                                        field === "homePhone"
                                                            ? "0xxx-xxx-xx-xx"
                                                            : field === "birthDate" || field === "hireDate"
                                                                ? "yyyy-mm-dd"
                                                                : ""
                                                    }
                                                    onChange={(e) => handleChange(field, e.target.value)}
                                                />
                                            )
                                        ) : (
                                            employee[field]
                                        )}
                                    </td>
                                ))}

                            <td>
                                {isEditing ? (
                                    <>
                                        <Button
                                            variant="primary"
                                            size="sm"
                                            onClick={() => handleSave(editingEmployee)}
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
                                            onClick={() => handleUpdate(employee)}
                                        >
                                            <FontAwesomeIcon icon={faArrowsRotate} />
                                        </Button>
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={() => handleDelete(employee.employeeId)}
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
    );
}
