const API_URL = "http://localhost:8080/api/order-details";

const handleResponse = async (response) => {
    const body = await response.json().catch(() => ({}));
    if (!response.ok) {
        throw new Error(body.message || "Unknown error!");
    }
    return body;
};

export const getOrderDetails = async () => {
    const response = await fetch(API_URL);
    return handleResponse(response);
};

export const addOrderDetail = async (data) => {
    const response = await fetch(`${API_URL}/add`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    return handleResponse(response);
};

export const updateOrderDetail = async (data) => {
    const response = await fetch(`${API_URL}/update`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    return handleResponse(response);
};

export const deleteOrderDetail = async (orderId, productId) => {
    const response = await fetch(`${API_URL}/?orderId=${orderId}&productId=${productId}`, {
        method: "DELETE",
    });
    return handleResponse(response);
};
