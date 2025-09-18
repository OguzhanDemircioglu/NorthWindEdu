import React from "react";
import {Container, Nav, NavLink} from "react-bootstrap";
import Navbar from "react-bootstrap/Navbar";
export default function VerticalManu() {
    return (
        <div className="verticalMenu">
            <Navbar bg="dark" variant="dark">
                <Container fluid>
                    <Nav style={{flex: "max-content"}}>
                            <>
                                <NavLink className="nav-link" href="/categories">
                                    Categories
                                </NavLink>
                            </>
                    </Nav>
                </Container>
            </Navbar>
        </div>
    );
}
