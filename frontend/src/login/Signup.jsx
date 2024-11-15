import React, { useState } from 'react';
import axios from 'axios';
import Header from './Header.jsx';
import Footer from './Footer.jsx';
import styles from './Signup.module.css';
import { useNavigate } from 'react-router-dom';

function Signup() {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    // Handle input changes
    const handleNameChange = (e) => setName(e.target.value);
    const handleEmailChange = (e) => setEmail(e.target.value);
    const handlePhoneChange = (e) => setPhone(e.target.value);
    const handlePasswordChange = (e) => setPassword(e.target.value);
    const handleConfirmPasswordChange = (e) => setConfirmPassword(e.target.value);

    // Handle form submission
    const handleSignup = (e) => {
        e.preventDefault();

        // Validate password and confirm password
        if (password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        const signupData = {
            username: name,
            email,
            phone,
            password,
            role: 'ROLE_USER'
        };

        axios.post('http://localhost:8081/api/auth/register', signupData)
            .then((response) => {
                if (response.data === 'User registered successfully!') {
                    console.log('Signup successful:', response.data);
                    navigate('/login');  // Redirect to login page after successful signup
                } else {
                    setError('Signup failed. Please try again.');
                }
            })
            .catch((error) => {
                console.error('Error during signup:', error);
                setError('An error occurred during signup. Please try again later.');
            });
    };

    return (
        <>
        <Header />
        <div className={styles.container}>
            <div className={styles.LoginCard}>
                <label>Name</label><br />
                <input
                    type="text"
                    id="name"
                    placeholder="Full Name"
                    value={name}
                    onChange={handleNameChange}
                /><br />

                <label>Email</label><br />
                <input
                    type="text"
                    id="InputEmail"
                    placeholder="Email"
                    value={email}
                    onChange={handleEmailChange}
                /><br />

                <label>Phone Number</label><br />
                <input
                    type="text"
                    id="phonenumber"
                    placeholder="Phone Number"
                    value={phone}
                    onChange={handlePhoneChange}
                /><br />

                <label>Password</label><br />
                <input
                    type="password"
                    id="InputPass"
                    placeholder="Password"
                    value={password}
                    onChange={handlePasswordChange}
                /><br />

                <label>Confirm Password</label><br />
                <input
                    type="password"
                    id="ConfirmPass"
                    placeholder="Confirm Password"
                    value={confirmPassword}
                    onChange={handleConfirmPasswordChange}
                /><br />

                {error && <p style={{ color: 'red' }}>{error}</p>} {/* Show error message if any */}

                <button onClick={handleSignup}>Sign up</button>
            </div>
        </div>
        <Footer />
        </>
    );
}

export default Signup;
