const API_URL = "http://localhost:8080/api/employee-territories";

const handleResponse = async (response) => {
    const body = await response.json().catch(() => ({}));
    if (!response.ok) {
        throw new Error(body.message || "Unknown error!");
    }
    return body;
};

export const getAllEmployeeTerritories = async () => {
    const response = await fetch(API_URL);
    return handleResponse(response);
};

export const addEmployeeTerritory = async (data) => {
    const response = await fetch(`${API_URL}/add`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    return handleResponse(response);
};

export const updateEmployeeTerritory = async (data) => {
    const response = await fetch(`${API_URL}/update`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    return handleResponse(response);
};

export const deleteEmployeeTerritory = async (employeeId, territoryId) => {
    const response = await fetch(
        `${API_URL}/?employeeId=${employeeId}&territoryId=${territoryId}`,
        { method: "DELETE" }
    );
    return handleResponse(response);
};