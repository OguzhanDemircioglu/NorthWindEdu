const API_URL = "http://localhost:8080/api/regions";

const handleResponse = async (response) => {
    const body = await response.json().catch(() => ({}));
    if (!response.ok) {
        throw new Error(body.message || "Unknown error!");
    }
    return body;
};

export const getAllRegions = async () => {
    const response = await fetch(API_URL);
    return handleResponse(response);
};

export const addRegion = async (data) => {
    const response = await fetch(`${API_URL}/add`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    return handleResponse(response);
};

export const updateRegion = async (data) => {
    const response = await fetch(`${API_URL}/update`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    return handleResponse(response);
};

export const deleteRegion = async (regionId) => {
    const response = await fetch(`${API_URL}/${regionId}`, {
        method: "DELETE",
    });
    return handleResponse(response);
};