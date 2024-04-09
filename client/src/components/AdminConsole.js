import React, {useEffect, useState} from 'react';
import {Button, Input} from "@mui/material";
import Store from "../store";
import "../App.css"
import UserService from "../services/UserService";

const AdminConsole = () => {
    const currentUser = Store.getState().user;

    const [items, setItems] = useState(null);
    const [searchKey, setSearchKey] = useState(null);
    const [deleteItem, setDeleteItem] = useState(null);
    const [editID, setEditID] = useState(null);

    const [formData, setFormData] = useState({
        id: '',
        username: '',
        email: '',
        role: ''
    });
    const [formUpdateData, setFormUpdateData] = useState({
        id: '',
        username: '',
        email: '',
        role: ''
    });

    useEffect(() => {
        UserService.findAllUsers((responseData) => setItems(responseData))
    }, []);

    function save(e) {
        e.preventDefault();
        if (formData.number === '' ||
            formData.name === '' ||
            formData.amount === '') {
            alert("Alanların hepsi dolu olmalı")
            return;
        }
        /*AccountService.save(formData);*/
        window.location.reload();
    }

    function deleteAccountByNumber(e) {
        e.preventDefault();
        /*AccountService.delete(deleteItem);*/
    }

    function updateAccount(e) {
 /*       e.preventDefault();
        formUpdateData.accountId = editID;

        if (formUpdateData.number === '' || formUpdateData.name === '') {
            alert("AccountNumber ve AccountName Boş olmamalı");
            return;
        }

        if (items.filter(i => i.number === formUpdateData.number || i.name === formUpdateData.name).length > 0) {
            alert("AccountNumber ve AccountName Öncekilerden farklı olmalı");
            return;
        }*/

     /*   AccountService.updateAccount(formUpdateData);*/
    }

    const handleInsert = (e) => {
        const {name, value} = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleUpdate = (e) => {
        const {name, value} = e.target;
        setFormUpdateData(prevState => ({
            ...prevState,
            [name]: value
        }));
        console.log(formUpdateData)
    };

    return (
        <div className="example">
            <Input type="text" placeholder="SearchByUserName..."
                   onChange={(e) => setSearchKey(e.target.value.toLowerCase())}/>
            <table id="table1">
                <thead>
                <tr>
                    <th></th>
                    <th>username</th>
                    <th>email</th>
                    <th>createdAt</th>
                    <th>updatedAt</th>
                    <th>role</th>
                    <th>ADD & DELETE</th>
                    <th>UPDATE</th>
                </tr>
                </thead>
                <tbody>
                {items?.filter(i => !searchKey || i.username.includes(searchKey)).map((item, index) => {
                        return (
                           /* editID === item.id ?
                                <tr>
                                    <td>{index + 1}</td>
                                    <td>
                                        <input
                                            type="text"
                                            name="username"
                                            value={formUpdateData.username}
                                            onChange={handleUpdate}
                                            placeholder="Name"
                                        />
                                    </td>

                                    <td>
                                        <input
                                            type="text"
                                            name="password"
                                            value={formUpdateData.password}
                                            onChange={handleUpdate}
                                            placeholder="Number"
                                        />
                                    </td>

                                    <td>
                                        <input
                                            type="text"
                                            name="email"
                                            value={formUpdateData.email}
                                            onChange={handleUpdate}
                                            placeholder="Balance"
                                        />
                                    </td>
                                    <td>
                                        <input
                                            type="text"
                                            name="role"
                                            value={formUpdateData.role}
                                            onChange={handleUpdate}
                                            placeholder="Balance"
                                        />
                                    </td>
                                    {item.createdAt ?
                                        <td>{item.createdAt[2]}
                                            .{item.createdAt[1]}
                                            .{item.createdAt[0]}
                                        </td> :
                                        <td/>
                                    }
                                    {item.updatedAt ?
                                        <td>{item.updatedAt[2]}
                                            .{item.updatedAt[1]}
                                            .{item.updatedAt[0]}
                                        </td> :
                                        <td/>
                                    }
                                    <td>
                                        <Button onClick={() => setEditID(null)}>CANCEL</Button>
                                    </td>
                                    <td>
                                        <Button onClick={updateAccount}>SUBMIT</Button>
                                    </td>
                                </tr>
                                :*/
                                <tr key={index}>
                                    <td>{index + 1}</td>
                                    <td>{item.username}</td>
                                    <td>{item.email}</td>
                                    {item.createdAt ?
                                        <td>{item.createdAt[2]}
                                            .{item.createdAt[1]}
                                            .{item.createdAt[0]}
                                        </td> :
                                        <td/>
                                    }
                                    {item.updatedAt ?
                                        <td>{item.updatedAt[2]}
                                            .{item.updatedAt[1]}
                                            .{item.updatedAt[0]}
                                        </td> :
                                        <td/>
                                    }
                                    <td>{item.role}</td>
                                    <td>
                                        <Button onMouseUp={() => setDeleteItem(item.number)}
                                                onClick={deleteAccountByNumber}>DELETE</Button>
                                    </td>
                                    <td>
                                        <Button onClick={() => setEditID(item.accountId)}>APDATE</Button>
                                    </td>
                                </tr>
                        )
                    })
                }
            {/*    <tr>
                    <td>{!items ? 1 : items.length + 1}</td>
                    <td>
                        <input
                            type="number"
                            name="number"
                            value={formData.number}
                            onChange={handleInsert}
                            placeholder="Number"
                        />
                    </td>
                    <td>
                        <input
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleInsert}
                            placeholder="Name"
                        />
                    </td>
                    <td>
                        <input
                            type="number"
                            name="amount"
                            value={formData.amount}
                            onChange={handleInsert}
                            placeholder="Balance"
                        />
                    </td>
                    <td/>
                    <td/>
                    <td>
                        <Button type={"submit"} onClick={save}>ADD</Button>
                    </td>
                    <td/>
                </tr>*/}
                </tbody>

            </table>

        {/*    <form onSubmit={beginTransaction}>
                <table id="table2">
                    <thead>
                    <tr>
                        <th>operationType</th>
                        <th>fromAccount</th>
                        <th>toAccount</th>
                        <th>amount</th>
                        <th>Transaction</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <select name="operationType" value={formTransaction.operationType}
                                    onChange={handleTransaction}>
                                <option value="">Operayon Type</option>
                                {Object.keys(Operation).map(key => Operation[key]).map(operationType => (
                                    <option key={operationType}
                                            value={operationType}>{operationType}</option>
                                ))}
                            </select>
                        </td>

                        <td>
                            <select name="fromAccountNumber" value={formTransaction.fromAccountNumber}
                                    onChange={handleTransaction}>
                                <option>Select Account</option>
                                {items?.filter(r => r.username.includes(currentUser?.username)).map(les => (
                                    <option key={les.number}
                                            value={les.number}>{les.username} - {les.number} - {les.name}</option>
                                ))}
                            </select>
                        </td>

                        {(formTransaction.operationType === '' ||
                                formTransaction.operationType === "WITHDRAWAL" ||
                                formTransaction.operationType === "DEPOSIT") &&
                            <td/>}

                        {formTransaction.operationType === "TRANSFER" &&
                            <td>
                                <select name="toAccountNumber" value={formTransaction.toAccountNumber}
                                        onChange={handleTransaction}>
                                    <option value="">Select Account</option>
                                    {items?.filter(r => r.username.includes(currentUser?.username)).map(les => (
                                        <option key={les.number}
                                                value={les.number}>{les.username} - {les.number} - {les.name}</option>
                                    ))}
                                </select>
                            </td>}

                        {formTransaction.operationType === "PAYMENT" &&
                            <td>
                                <select name="toAccountNumber" value={formTransaction.toAccountNumber}
                                        onChange={handleTransaction}>
                                    <option value="">Select Account</option>
                                    {items?.filter(r => !r.username.includes(currentUser?.username)).map(les => (
                                        <option key={les.number}
                                                value={les.number}>{les.username} - {les.number} - {les.name}</option>
                                    ))}
                                </select>
                            </td>}
                        <td>
                            <input
                                type="number"
                                name="amount"
                                value={formTransaction.amount}
                                onChange={handleTransaction}
                                placeholder="Amount"
                            />
                        </td>
                        <td>
                            <Button type="submit">Begin Transaction</Button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>*/}
        </div>
    );
};


export default AdminConsole;