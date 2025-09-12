import React from "react";
import { Nav } from "react-bootstrap";

export default function VerticalManu() {
    return (
        <div
            style={{
                width: "220px",
                height: "100vh",
                backgroundColor: "#f8f9fa",
                borderRight: "1px solid #ddd",
                paddingTop: "20px",
                position: "fixed",
                left: 0,
                top: 56,
            }}
        >
            <Nav className="flex-column" style={{ paddingLeft: "20px" }}>
                <Nav.Link href="/categories">Categories</Nav.Link>
            </Nav>
        </div>
    );
}
