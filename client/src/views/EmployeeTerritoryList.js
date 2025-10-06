import React, { useEffect, useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faAdd, faSave, faTrash, faCancel, faSearch, faRotateRight} from "@fortawesome/free-solid-svg-icons";
import {getAllEmployeeTerritories, addEmployeeTerritory, updateEmployeeTerritory, deleteEmployeeTerritory} from "../services/EmployeeTerritoryService";

export default function EmployeeTerritoryList() {
    const [territories, setTerritories] = useState([]);
    const [editing, setEditing] = useState(null);
    const [searchText, setSearchText] = useState("");
    const [allData, setAllData] = useState([]);

    const loadData = async () => {
        try {
            const res = await getAllEmployeeTerritories();
            setTerritories(res.data || []);
            setAllData(res.data || []);
        } catch (e) {
            setTerritories([]);
        }
    };

    useEffect(() => {
        loadData();
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
            <h3>Employee Territories</h3>

            <Form className="d-flex mb-3" onSubmit={handleSearch}>
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

            <Button variant="success" className="mb-3" onClick={handleAdd}>
                <FontAwesomeIcon icon={faAdd} />
            </Button>

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>Employee ID</th>
                    <th>Territory ID</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {editing && (
                    <tr>
                        <td>
                            <input
                                type="text"
                                value={editing.employeeId}
                                onChange={(e) =>
                                    setEditing({ ...editing, employeeId: e.target.value })
                                }
                            />
                        </td>
                        <td>
                            <input
                                value={editing.territoryId}
                                onChange={(e) =>
                                    setEditing({ ...editing, territoryId: e.target.value })
                                }
                            />
                        </td>
                        <td>
                            <Button
                                variant="primary"
                                size="sm"
                                onClick={() => handleSave(editing)}
                            >
                                <FontAwesomeIcon icon={faSave} />
                            </Button>
                            <Button
                                variant="secondary"
                                size="sm"
                                className="ms-2"
                                onClick={() => setEditing(null)}
                            >
                                <FontAwesomeIcon icon={faCancel} />
                            </Button>
                        </td>
                    </tr>
                )}
                {territories.map((t, i) => (
                    <tr key={i}>
                        <td>{t.employeeId}</td>
                        <td>{t.territoryId}</td>
                        <td>
                            <Button
                                variant="danger"
                                size="sm"
                                onClick={() =>
                                    handleDelete(t.employeeId, t.territoryId)
                                }
                            >
                                <FontAwesomeIcon icon={faTrash} />
                            </Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </div>
    );
}