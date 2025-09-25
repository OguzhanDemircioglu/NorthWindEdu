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
                        <NavLink className="nav-link" href="/customer-demographics">
                            Customer Demographics
                        </NavLink>
                        <NavLink className="nav-link" href="/customer-demos">
                            Customer Demos
                        </NavLink>
                        <NavLink className="nav-link" href="/employee-territories">
                            Employee Territory
                        </NavLink>
                        <NavLink className="nav-link" href="/territories">
                            Territory
                        </NavLink>
                        <NavLink className="nav-link" href="/products">
                            Product
                        </NavLink>
                        <NavLink className="nav-link" href="/regions">
                            Region
                        </NavLink>
                    </Nav>
                </Container>
            </Navbar>
        </div>
    );
}
