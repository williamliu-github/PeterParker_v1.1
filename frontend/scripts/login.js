import { config, artifact } from './config.js';


/*-------------------------------------------*/
/*Login
/*-------------------------------------------*/


document.getElementById('login_form').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    // let formData = new FormData(this); // Collect form data
    // let jsonData = {};
    let errorMessage = document.getElementById('errorMessage');
    let userAccount = document.getElementById('login_user_account');
    let userPassword = document.getElementById('login_user_password');
    let isValid = true;
    let rememberMe = document.getElementById('login_remember_me');

    errorMessage.style.display = "none";


    // Validate inputs
    if (userAccount.value.trim() === "") {
        errorMessage.style.display = "block";
        errorMessage.innerText = "請輸入您的郵件信箱。";
        isValid = false;
    } else if (userPassword.value.trim() === "") {
        errorMessage.style.display = "block";
        errorMessage.innerText = "請輸入您的帳號密碼。";
        isValid = false;
    }

    // Convert form data to JSON format
    if (isValid) {
  

        const loginData = {
            user_account: userAccount.value,
            user_password: userPassword.value,
            remember_me: rememberMe.checked // This will be true or false
        };
        
        // Send the form data as JSON using the fetch API
        fetch(`http://${config}/${artifact}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        })
        .then(response => {
            if (response.ok) {
                return response.json(); // Convert the response to JSON
            } else if (response.status === 404) {
                errorMessage.style.display = "block";
                errorMessage.innerText = "輸入的帳號密碼有誤，請重新輸入。";
                throw new Error('Invalid credentials');
            } else {
                console.error('Unexpected response:', response.status);
                throw new Error(`Unexpected status code: ${response.status}`);
            }
        })
        .then(data => {
            // Check if the jwtToken is present in the response
            if (data.jwtToken) {
                // Store the JWT in localStorage
                localStorage.setItem('peterParkerToken', data.jwtToken);
                console.log('Token saved in localStorage:', data.jwtToken);
                window.location.href = 'user_center.html'; // Redirect on success
            } else {
                console.error('JWT not found in response');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }});


/*-------------------------------------------*/
/*Registration
/*-------------------------------------------*/


document.getElementById('register_form').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    let formData_registry = new FormData(this); // Collect form data
    const jsonData_registry = {};
	let userName_registry = document.getElementById('username_registry');
	let userPhone_registry = document.getElementById('userphone_registry');
	let userAccount_registry=document.getElementById('useraccount_registry');
	let userPassword_registry = document.getElementById('userpassword_registry');
	let carNumber_registry = document.getElementById('carNumber_registry');
	let errorMessage_registry = document.getElementById('errorMessageRegistry');
	let isFilled = true;


	if(userName_registry.value.trim() === "") {
		errorMessage_registry.style.display = "block";
		errorMessage_registry.innerText = "請輸入您的名字。";
		isFilled = false;
	} else if (userPhone_registry.value.trim() === "") {
		errorMessage_registry.style.display = "block";
		errorMessage_registry.innerText = "請輸入您的電話。";
		isFilled = false;
	} else if (userAccount_registry.value.trim() === ""){
		errorMessage_registry.style.display = "block";
		errorMessage_registry.innerText = "請輸入您的郵件信箱。";
		isFilled = false;
	} else if (userPassword_registry.value.trim()===""){
		errorMessage_registry.style.display = "block";
		errorMessage_registry.innerText = "請輸入您的密碼。";
		isFilled = false;
	} 

    // Convert form data to JSON format
    formData_registry.forEach((value, key) => {
        jsonData_registry[key] = value;
    });

    // Send the form data as JSON using the fetch API
    if(isFilled){
		fetch(`http://${config}/${artifact}/addUser`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(jsonData_registry)
		})
		.then(response => response.json()) // Parse the JSON response
		.then(data => {
			if (data.message === "Verification email successfully sent") {
				// Redirect to the user_center.html page on successful login
				window.location.href = 'user_verify_code.html';} 
				else if(data.message == "User already exists"){
					errorMessage_registry.style.display = 'block';
					errorMessage_registry.innerText = "帳號已註冊，請登入使用。"
			} else { 
				// Handle any error messages (e.g., invalid credentials)
				errorMessage_registry.style.display = 'block';
				errorMessage_registry.innerText = "輸入的帳號密碼有誤，請重新輸入。"
				
			}
		})
		.catch((error) => {
			console.error('Error:', error);
			console.error(JSON.stringify(jsonData));
			errorMessage_registry.style.display = 'block';
			errorMessage_registry.innerText = "系統忙線中，請等候。"
		})
	}
	
})

