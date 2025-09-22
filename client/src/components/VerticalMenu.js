import React from "react";
import { Container, Nav, NavLink } from "react-bootstrap";
import Navbar from "react-bootstrap/Navbar";

export default function VerticalMenu() {
    return (
        <div className="verticalMenu">
            <Navbar bg="dark" variant="dark">
                <Container fluid>
                    <Nav style={{ flex: "max-content" }} className="flex-column">
                        <NavLink className="nav-link" href="/categories">
                            Categories
                        </NavLink>
                        <NavLink className="nav-link" href="/order-details">
                            Order Details
                        </NavLink>
                    </Nav>
                </Container>
            </Navbar>
        </div>
    );
}
