import React, { useState, useEffect } from 'react';
import { FaAddressCard, FaCreditCard, FaHome, FaHeartbeat, FaUsers, FaLock } from 'react-icons/fa';  
import { useNavigate } from 'react-router-dom';
import styles from './UserDB.module.css';
import Headern from '../landing/Headern';
import Footer from '../login/Footer';

const UserDB = () => {
    const navigate = useNavigate();

    // Initialize sections based on local storage values or default to false
    const initialSections = [
        { id: 1, title: 'Contact Information', description: 'Update your personal contact information.', icon: <FaAddressCard />, completed: JSON.parse(localStorage.getItem('contactinformation')) || false },
        { id: 2, title: 'Payment Methods', description: 'Manage your saved payment methods securely.', icon: <FaCreditCard />, completed: JSON.parse(localStorage.getItem('paymentinformation')) || false },
        { id: 3, title: 'Delivery Address', description: 'Set up your preferred delivery locations.', icon: <FaHome />, completed: JSON.parse(localStorage.getItem('deliveryinformation')) || false },
        { id: 4, title: 'Health Conditions', description: 'Update your health profile and relevant information.', icon: <FaHeartbeat />, completed: JSON.parse(localStorage.getItem('healthconditionsinformation')) || false },
        { id: 5, title: 'Add Dependents', description: 'Add dependents who are linked to your account.', icon: <FaUsers />, completed: JSON.parse(localStorage.getItem('dependentsinformation')) || false },
        { id: 6, title: 'Security Settings', description: 'Manage your account passwords including 2FA.', icon: <FaLock />, completed: JSON.parse(localStorage.getItem('securityinformation')) || false }
    ];

    const [sections, setSections] = useState(initialSections);

    // Function to mark a section as completed or navigate
    const completeSection = (id) => {
        let updatedSections = sections.map((section) => {
            if (section.id === id) {
                section.completed = true;
                // Update the local storage for the specific section
                if (id === 1) {
                    localStorage.setItem('contactInfo', 'true');
                } else if (id === 2) {
                    localStorage.setItem('paymentInfo', 'true');
                } else if (id === 3) {
                    localStorage.setItem('deliveryInfo', 'true');
                } else if (id === 4) {
                    localStorage.setItem('healthConditions', 'true');
                } else if (id === 5) {
                    localStorage.setItem('dependentsInfo', 'true');
                } else if (id === 6) {
                    localStorage.setItem('securityInfo', 'true');
                }
            }
            return section;
        });

        setSections(updatedSections);

        // Navigate to the appropriate form based on section
        if (id === 1) {
            navigate('/contact-form');
        } else if (id === 2) {
            navigate('/payment-form');
        } else if (id === 3) {
            navigate('/address-form');
        } else if (id === 4) {
            navigate('/health-conditions-form');
        } else if (id === 5) {
            navigate('/dependencies-form');
        } else if (id === 6) {
            navigate('/security-settings-form');
        }
    };

    return (
        <>
        <Headern/>
        <div className={styles.dbcontainer}>
            <h2 className={styles.dbheader}>Member Profile Setup Page</h2>
            <p className={styles.description}>Complete each section to ensure your profile is up-to-date.</p>
            <div className={styles.dbcardsContainer}>
                {sections.map((section) => (
                    <div 
                        key={section.id} 
                        className={`${styles.dbcard} ${section.completed ? styles.completed : styles.uncompleted}`}
                    >
                        <div className={styles.iconContainer}>{section.icon}</div>
                        <h3>{section.title}</h3>
                        <p>{section.description}</p>
                        <p className={styles.status}>Status: <span className={section.completed ? styles.statusCompleted : styles.statusUncompleted}>{section.completed ? 'Completed' : 'Uncompleted'}</span></p>
                        <button
                            onClick={() => completeSection(section.id)}
                            disabled={section.completed}
                            className={styles.dbbutton}
                        >
                            {section.completed ? 'Completed' : 'Complete Section'}
                        </button>
                        {section.completed && (
                            <div className={styles.loginMessage}>Login to edit</div>
                        )}
                    </div>
                ))}
            </div>
        </div>
        <Footer/>
        </>
    );
};

export default UserDB;
