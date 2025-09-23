const API_URL = "http://localhost:8080/api/customer-demos";

const handleResponse = async (response) => {
    const body = await response.json().catch(() => ({}));
    if (!response.ok) {
        throw new Error(body.message || "Unknown error!");
    }
    return body;
};

export const getAllCustomerDemos = async () => {
    const response = await fetch(API_URL);
    return handleResponse(response);
};

export const addCustomerDemo = async (data) => {
    const response = await fetch(`${API_URL}/add`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    return handleResponse(response);
};

export const updateCustomerDemo = async (data) => {
    const response = await fetch(`${API_URL}/update`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    return handleResponse(response);
};

export const deleteCustomerDemo = async (customerId, customerTypeId) => {
    const response = await fetch(
        `${API_URL}/?customerId=${customerId}&customerTypeId=${customerTypeId}`,
        { method: "DELETE" }
    );
    return handleResponse(response);
};