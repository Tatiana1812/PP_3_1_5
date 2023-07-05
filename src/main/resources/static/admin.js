let tableUsers = [];
let roles_id = [];
let deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
let editModal = new bootstrap.Modal(document.getElementById('editModal'));
let request = new Request("/api/admin", {
    method: "GET",
    headers: {
        'Content-Type': 'application/json',
    },
});
getUsers()
function getUsers() {
    fetch(request).then(res =>
        res.json()).then(data => {
        tableUsers = [];
        if (data.length > 0) {
            data.forEach(user => {
                tableUsers.push(user)
            })
        } else {
            tableUsers = [];
        }

        showUsers(tableUsers);
    })
}
function showUsers(table) {
    let temp = "";
    table.forEach(user => {
        temp += "<tr>"
        temp += "<td>" + user.id + "</td>"
        temp += "<td>" + user.name + "</td>"
        temp += "<td>" + user.lastName + "</td>"
        temp += "<td>" + user.age + "</td>"
        temp += "<td>" + user.roles.map(role => role.roleName) + "</td>"
        temp += "<td>" + `<button type="button" class="btn btn-info" onclick='edit_user(${user.id})'>Edit</button>` + "</td>"
        temp += "<td>" + `<button type="button" class="btn btn-danger" onclick='delete_user(${user.id})'>Delete</button>`+ "</td>"
        temp += "</tr>"
        document.getElementById("allUsersBody").innerHTML = temp;
    })
}
function getRoles(list) {
    let userRoles = [];
    for (let role of list) {
        if (role === 2 || role.id === 2) {
            userRoles.push("ADMIN");
        }
        if (role === 1 || role.id === 1) {
            userRoles.push("USER");
        }
    }
    return userRoles.join(" , ");
}
function delete_user(id) {
    document.getElementById('closeDeleteModal').setAttribute('onclick', () => {
        deleteModal.hide();
        document.getElementById('deleteUser').reset();
    });

    let request = new Request("/api/admin/" + id, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    fetch(request).then(res => res.json()).then(deleteUser => {
            console.log(deleteUser);
            document.getElementById('idDel').setAttribute('value', deleteUser.id);
            document.getElementById('firstNameDel').setAttribute('value', deleteUser.name);
            document.getElementById('lastNameDel').setAttribute('value', deleteUser.lastName);
            document.getElementById('ageDel').setAttribute('value', deleteUser.age);
            document.getElementById('passwordDel').setAttribute('value', deleteUser.password);
            if (getRoles(deleteUser.roles).includes("USER") && getRoles(deleteUser.roles).includes("ADMIN")) {
                document.getElementById('rolesDel1').setAttribute('selected', 'true');
                document.getElementById('rolesDel2').setAttribute('selected', 'true');
            } else if (getRoles(deleteUser.roles).includes("USER")) {
                document.getElementById('rolesDel1').setAttribute('selected', 'true');
            } else if (getRoles(deleteUser.roles).includes("ADMIN")) {
                document.getElementById('rolesDel2').setAttribute('selected', 'true');
            }
            deleteModal.show();
        }
    );
    var isDelete = false;
    document.getElementById('deleteUser').addEventListener('submit', event => {
        event.preventDefault();
        if (!isDelete) {
            isDelete = true;
            let request = new Request("/api/admin/" + id, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            fetch(request).then(() => {
                getUsers();
            });
            document.getElementById('deleteUser').reset();
        }

        deleteModal.hide();
    });
}

function edit_user(id) {
    let request = new Request("/api/admin/" + id, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });
    fetch(request).then(res => res.json()).then(editUser => {
            document.getElementById('idRed').setAttribute('value', editUser.id);
            document.getElementById('firstNameRed').setAttribute('value', editUser.name);
            document.getElementById('lastNameRed').setAttribute('value', editUser.lastName);
            document.getElementById('ageRed').setAttribute('value', editUser.age);
            document.getElementById('passwordRed').setAttribute('value', editUser.password);
            if ((editUser.roles.map(role => role.id))%2 === 1 && ((editUser.roles.map(role => role.id))%2 === 2)) {
                document.getElementById('rolesRed1').setAttribute('selected', 'true');
                document.getElementById('rolesRed2').setAttribute('selected', 'true');
            } else if ((editUser.roles.map(role => role.id))%2 === 1) {
                document.getElementById('rolesRed1').setAttribute('selected', 'true');
            } else if (editUser.roles.map(role => role.id)%2 === 2) {
                document.getElementById('rolesRed2').setAttribute('selected', 'true');
            }
            editModal.show();
        }
    );

    document.getElementById('editUser').addEventListener('submit', submitFormEditUser);
}
function submitFormEditUser(event) {
    event.preventDefault();
    let redUserForm = new FormData(event.target);
    const form_ed = document.getElementById("rolesRed");
    let adminRole = {id:2, role:"ROLE_ADMIN"};
    let userRole = {id:1, role:"ROLE_USER"};
    let setRoles = [];
    if (form_ed.options[0].selected){
        setRoles.push(userRole)
    }
    if(form_ed.options[1].selected){
        setRoles.push(adminRole)
    }
    let user = {
        id: redUserForm.get('id'),
        name: redUserForm.get('name'),
        lastName: redUserForm.get('lastName'),
        age: redUserForm.get('age'),
        password: redUserForm.get('password'),
        roles:setRoles
    }
    console.log(user);
    let request = new Request("/api/admin", {
        method: 'PATCH',
        body: JSON.stringify(user),
        headers: {
            'Content-Type': 'application/json',
        },
    });
    fetch(request).then(
        function (response) {
            console.log(response)
            getUsers();
            event.target.reset();
            editModal.hide();
        });
}