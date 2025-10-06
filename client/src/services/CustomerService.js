const API_URL = "http://localhost:8080/api/customers";

const handleResponse = async (response) => {
    const body = await response.json().catch(() => ({}));
    if (!response.ok) {
        throw new Error(body.message || "Unknown error!");
    }
    return body;
};

export const getCustomers = async () => {
    const response = await fetch(API_URL);
    return handleResponse(response);
};

export const addCustomer = async (data) => {
    const response = await fetch(`${API_URL}/add`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    return handleResponse(response);
};

export const updateCustomer = async (data) => {
    const response = await fetch(`${API_URL}/update`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    return handleResponse(response);
};

export const deleteCustomer = async (id) => {
    const response = await fetch(`${API_URL}/${id}`, {
        method: "DELETE",
    });
    return handleResponse(response);
};