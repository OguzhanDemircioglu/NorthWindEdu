import React, { useEffect, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faAdd, faSave, faTrash, faCancel, faSearch, faRotateRight} from "@fortawesome/free-solid-svg-icons";
import {getAllEmployeeTerritories, addEmployeeTerritory, updateEmployeeTerritory, deleteEmployeeTerritory} from "../services/EmployeeTerritoryService";
import { getEmployees } from "../services/EmployeeService";
import { getAllTerritories } from "../services/TerritoryService";

export default function EmployeeTerritoryList() {
    const [territories, setTerritories] = useState([]);
    const [allData, setAllData] = useState([]);
    const [editing, setEditing] = useState(null);
    const [searchText, setSearchText] = useState("");

    const [employees, setEmployees] = useState([]);
    const [territoryOptions, setTerritoryOptions] = useState([]);

    const loadData = async () => {
        try {
            const res = await getAllEmployeeTerritories();
            setTerritories(res.data || []);
            setAllData(res.data || []);
        } catch (e) {
            setTerritories([]);
        }
    };

    const loadLookups = async () => {
        try {
            const [employee, territory] = await Promise.all([
                getEmployees(),
                getAllTerritories(),
            ]);
            setEmployees(employee.data || []);
            setTerritoryOptions(territory.data || []);
        } catch (err) {
            console.error("Error loading data:", err);
        }
    };

    useEffect(() => {
        loadData();
        loadLookups();
    }, []);

    const handleAdd = () => {
        if (editing) return;
        setEditing({ employeeId: "", territoryId: "" });
    };

    const handleSave = async (item) => {
        try {
            const exists = allData.find(
                (d) =>
                    d.employeeId === parseInt(item.employeeId) &&
                    d.territoryId === item.territoryId
            );

            if (exists) {
                await updateEmployeeTerritory(item);
            } else {
                await addEmployeeTerritory(item);
            }
            setEditing(null);
            loadData();
        } catch (e) {
            alert("Save failed: " + e.message);
        }
    };

    const handleDelete = async (employeeId, territoryId) => {
        if (window.confirm("Are you sure you want to delete?")) {
            try {
                await deleteEmployeeTerritory(employeeId, territoryId);
                loadData();
            } catch (e) {
                alert(e.message);
            }
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        if (!searchText) {
            setTerritories(allData);
            return;
        }

        const filtered = allData.filter((d) =>
            Object.values(d).some((value) =>
                value?.toString().toLowerCase().includes(searchText.toLowerCase())
            )
        );

        setTerritories(filtered);
    };

    return (
        <div style={{ padding: "20px" }}>
            <div style={{ display: "flex", justifyContent: "center", alignItems: "center", marginBottom: "20px" }}>
                <h3 className="me-2">Employee Territories</h3>

                <div className="d-flex align-items-center">
                    <Form className="d-flex" onSubmit={handleSearch}>
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
                                setTerritories(allData);
                            }}
                        >
                            <FontAwesomeIcon icon={faRotateRight} />
                        </Button>
                    </Form>

                    <Button variant="success" className="ms-3" onClick={handleAdd}>
                        <FontAwesomeIcon icon={faAdd} />
                    </Button>
                </div>
            </div>

            <div className="table-wrapper" style={{ display: "flex", justifyContent: "center" }}>
                <Table striped bordered hover className="table-compact" style={{ maxWidth: "700px" }}>
                    <thead>
                    <tr>
                        <th>Employee</th>
                        <th>Territory</th>
                        <th className="actions-col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {editing && (
                        <tr>
                            <td>
                                <Form.Select
                                    value={editing.employeeId || ""}
                                    onChange={(e) =>
                                        setEditing({ ...editing, employeeId: e.target.value })
                                    }
                                >
                                    <option value="">Select...</option>
                                    {employees.map((emp) => (
                                        <option key={emp.employeeId} value={emp.employeeId}>
                                            {emp.firstName} {emp.lastName}
                                        </option>
                                    ))}
                                </Form.Select>
                            </td>
                            <td>
                                <Form.Select
                                    value={editing.territoryId || ""}
                                    onChange={(e) =>
                                        setEditing({ ...editing, territoryId: e.target.value })
                                    }
                                >
                                    <option value="">Select...</option>
                                    {territoryOptions.map((t) => (
                                        <option key={t.territoryId} value={t.territoryId}>
                                            {t.territoryDescription}
                                        </option>
                                    ))}
                                </Form.Select>
                            </td>
                            <td>
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
                                    onClick={() => setEditing(null)}
                                >
                                    <FontAwesomeIcon icon={faCancel} />
                                </Button>
                            </td>
                        </tr>
                    )}
                    {territories.map((t, i) => {
                        const emp = employees.find((e) => e.employeeId === t.employeeId);
                        const terr = territoryOptions.find((x) => x.territoryId === t.territoryId);

                        return (
                            <tr key={i}>
                                <td>{emp ? `${emp.firstName} ${emp.lastName}` : t.employeeId}</td>
                                <td>{terr ? terr.territoryDescription : t.territoryId}</td>
                                <td style={{ textAlign: 'center' }}>
                                <Button
                                        variant="danger"
                                        size="sm"
                                        className="btn-compact"
                                        onClick={() => handleDelete(t.employeeId, t.territoryId)}
                                    >
                                        <FontAwesomeIcon icon={faTrash} />
                                    </Button>
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