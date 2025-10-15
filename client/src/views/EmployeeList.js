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

const sortById = (data) => {
    return data.sort((a, b) => a.employeeId - b.employeeId);
};

function reducer(state, action) {
    switch (action.type) {
        case "SET_ALL":
            return sortById(action.payload || []);
        default:
            return state;
    }
}

export default function EmployeeList() {
    const [employees, dispatch] = useReducer(reducer, initialState);
    const [updateId, setUpdateId] = useState(null);
    const [editingEmployee, setEditingEmployee] = useState(null);
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

        if (value instanceof Date) {
            const yyyy = value.getFullYear();
            const mm = String(value.getMonth() + 1).padStart(2, "0");
            const dd = String(value.getDate()).padStart(2, "0");
            return `${yyyy}-${mm}-${dd}`;
        }
        const strValue = value.toString().trim();
        const parts = strValue.split(/[-,]/);
        if (parts.length === 3) {
            const [y, m, d] = parts;
            return `${y.padStart(4, "0")}-${m.padStart(2, "0")}-${d.padStart(2, "0")}`;
        }
        const digits = strValue.replace(/\D/g, "").slice(0, 8);
        if (digits.length === 8) {
            const year = digits.slice(0, 4);
            const month = digits.slice(4, 6);
            const day = digits.slice(6, 8);
            return `${year}-${month}-${day}`;
        }

        return strValue;
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
            Object.values(emp).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );

        dispatch({ type: "SET_ALL", payload: filtered });
    };

    return (
        <div style={{ padding: "20px" }}>
            <div style={{ textAlign: "center", marginBottom: "20px" }}>
                <h3 style={{color: '#343a40', fontWeight: '600', paddingBottom: '5px', borderBottom: '3px solid #6c757d', textTransform: 'uppercase', letterSpacing: '1.5px', marginBottom: '15px'}}>
                    EMPLOYEES
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
                            dispatch({ type: "SET_ALL", payload: allEmployees });
                        }}
                        title="Reset"
                    >
                        <FontAwesomeIcon icon={faRotateRight} />
                    </Button>
                <Button variant="success" className="ms-3" onClick={handleAdd} title="Add">
                    <FontAwesomeIcon icon={faAdd} />
                </Button>
                </Form>
            </div>

            <div className="table-wrapper" style={{ display: "flex", justifyContent: "center" }}>
                <Table striped bordered hover className="table-compact" style={{ maxWidth: "700px" }}>
                    <thead>
                    <tr>
                        <th className="id-col">ID</th>
                        <th>Last Name</th>
                        <th>First Name</th>
                        <th>Title</th>
                        <th>Courtesy</th>
                        <th>Birth Date</th>
                        <th>Hire Date</th>
                        <th>Phone</th>
                        <th className="actions-col">Actions</th>
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
                                        ) : field === "birthDate" || field === "hireDate" ? (
                                            <input
                                                type="date"
                                                value={editingEmployee[field] || ""}
                                                onChange={(e) => handleChange(field, e.target.value)}
                                            />
                                        ) : (
                                            <input
                                                type={field === "homePhone" ? "tel" : "text"}
                                                value={editingEmployee[field] || ""}
                                                placeholder={field === "homePhone" ? "0xxx-xxx-xx-xx" : ""}
                                                onChange={(e) => handleChange(field, e.target.value)}
                                            />
                                        )}
                                    </td>
                                ))}

                            <td>
                                <Button
                                    variant="primary"
                                    size="sm"
                                    className="btn-compact me-2"
                                    onClick={() => handleSave(editingEmployee)}
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
                                                        <option value="Dr.">Dr.
                                                        </option>
                                                    </Form.Select>
                                                ) : field === "birthDate" || field === "hireDate" ? (
                                                    <input
                                                        type="date"
                                                        value={editingEmployee[field] || ""}
                                                        onChange={(e) => handleChange(field, e.target.value)}
                                                    />
                                                ) : (
                                                    <input
                                                        type={field === "homePhone" ? "tel" : "text"}
                                                        value={editingEmployee[field] || ""}
                                                        placeholder={field === "homePhone" ? "0xxx-xxx-xx-xx" : ""}
                                                        onChange={(e) => handleChange(field, e.target.value)}
                                                    />
                                                )
                                            ) : field === "birthDate" || field === "hireDate" ? (
                                                formatDate(employee[field])
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
                                                className="btn-compact me-2"
                                                onClick={() => handleSave(editingEmployee)}
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
                                                onClick={() => handleUpdate(employee)}
                                                title="Update"
                                            >
                                                <FontAwesomeIcon icon={faArrowsRotate} />
                                            </Button>
                                            <Button
                                                variant="danger"
                                                size="sm"
                                                className="btn-compact"
                                                onClick={() => handleDelete(employee.employeeId)}
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