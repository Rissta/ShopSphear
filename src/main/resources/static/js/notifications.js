
function showNotification(message, type = 'info') {
    let container = document.getElementById('notificationContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'notificationContainer';
        container.className = 'position-fixed top-0 end-0 p-3';
        container.style.zIndex = '9999';
        document.body.appendChild(container);
    }

    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <div class="d-flex justify-content-between align-items-start">
            <span class="text-white">${message}</span>
            <button type="button" class="btn-close btn-close-white ms-2" onclick="this.parentElement.parentElement.remove()"></button>
        </div>
    `;

    container.appendChild(notification);

    setTimeout(() => {
        if (notification.parentElement) {
            notification.style.opacity = '0';
            notification.style.transform = 'translateX(100%)';
            setTimeout(() => {
                if (notification.parentElement) {
                    notification.remove();
                }
            }, 300);
        }
    }, 5000);
}

const notificationStyles = `
.notification {
    background: var(--bs-second);
    border: 1px solid var(--bs-primary);
    border-radius: 8px;
    padding: 1rem;
    margin-bottom: 1rem;
    min-width: 300px;
    animation: slideIn 0.3s ease-out;
    transition: all 0.3s ease;
}

.notification.success {
    border-color: var(--bs-success);
}

.notification.error {
    border-color: var(--bs-danger);
}

.notification.warning {
    border-color: var(--bs-warning);
}

@keyframes slideIn {
    from { transform: translateX(100%); opacity: 0; }
    to { transform: translateX(0); opacity: 1; }
}
`;

if (!document.getElementById('notificationStyles')) {
    const styleSheet = document.createElement('style');
    styleSheet.id = 'notificationStyles';
    styleSheet.textContent = notificationStyles;
    document.head.appendChild(styleSheet);
}