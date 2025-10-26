function deleteProduct(cartItemId) {
    if (confirm('Вы уверены, что хотите удалить товар из корзины?')) {
        fetch('/cart/delete-by-id', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'cartItemId=' + cartItemId
        })
        .then(response => {
            if (response.ok) {
                const cartItem = document.querySelector(`button[onclick*="deleteProduct(${cartItemId})"]`).closest('.cart-item');
                cartItem.style.opacity = '0';
                cartItem.style.transform = 'translateX(100%)';
                setTimeout(() => {
                    cartItem.remove();
                    updateCartUI();
                    showNotification('Товар удален из корзины', 'success');
                }, 300);
            } else {
                throw new Error('Ошибка при удалении товара');
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            showNotification('Ошибка при удалении товара', 'error');
        });
    }
}

function deleteAllProducts() {
    if (confirm('Вы уверены, что хотите очистить всю корзину?')) {
        fetch('/cart/go-to-buy', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        })
        .then(response => {
            if (response.ok) {
                clearCartUI();
                showNotification('Корзина очищена', 'success');
            } else {
                throw new Error('Ошибка при очистке корзины');
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            showNotification('Ошибка при очистке корзины', 'error');
        });
    }
}

function increaseQuantity(cartItemId) {
    fetch('/cart/increase-quantity', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'cartItemId=' + cartItemId
    })
    .then(response => {
        if (response.ok) {
            showNotification('Количество увеличено', 'success');
            setTimeout(() => {
                location.reload();
            }, 500);
        } else {
            throw new Error('Ошибка при увеличении количества');
        }
    })
    .catch(error => {
        console.error('Ошибка:', error);
        showNotification('Ошибка при увеличении количества', 'error');
    });
}

function decreaseQuantity(cartItemId) {
    fetch('/cart/decrease-quantity', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'cartItemId=' + cartItemId
    })
    .then(response => {
        if (response.ok) {
            showNotification('Количество уменьшено', 'success');
            setTimeout(() => {
                location.reload();
            }, 500);
        } else {
            throw new Error('Ошибка при уменьшении количества');
        }
    })
    .catch(error => {
        console.error('Ошибка:', error);
        showNotification('Ошибка при уменьшении количества', 'error');
    });
}


function updateCartUI() {
    const cartItems = document.querySelectorAll('.cart-item');
    const itemCount = cartItems.length;

    updateItemCount(itemCount);

    if (itemCount === 0) {
        showEmptyCartMessage();
    } else {
        const orderSummary = document.querySelector('aside.col-lg-4');
        if (orderSummary) {
            orderSummary.style.display = 'block';
        }
    }
}

// Функция полной очистки корзины
function clearCartUI() {
    const cartItemsContainer = document.getElementById('cartItemsContainer');
    if (cartItemsContainer) {
        cartItemsContainer.innerHTML = '';
    }

    updateItemCount(0);
    showEmptyCartMessage();
}

function updateItemCount(count) {
    const countElement = document.querySelector('.text-center.mb-5 p');
    if (countElement) {
        if (count === 0) {
            countElement.style.display = 'none';
        } else {
            countElement.style.display = 'block';
            countElement.textContent = count + ' товара в корзине';
        }
    }
}

function showEmptyCartMessage() {
    const cartSection = document.querySelector('section.col-lg-8');
    const orderSummary = document.querySelector('aside.col-lg-4');
    const emptyCartMessage = document.querySelector('.col-12.text-center');

    if (orderSummary) {
        orderSummary.style.display = 'none';
    }

    if (emptyCartMessage) {
        emptyCartMessage.style.display = 'block';

        const row = emptyCartMessage.closest('.row');
        if (row) {
            const aside = row.querySelector('aside.col-lg-4');
            if (aside) {
                aside.style.display = 'none';
            }
            const section = row.querySelector('section.col-lg-8');
            if (section) {
                section.className = 'col-12';
            }
        }
    }
    else if (cartSection) {
        cartSection.className = 'col-12';
        cartSection.innerHTML = `
            <div class="col-12 text-center empty-cart-message">
                <div class="card bg-second border-primary">
                    <div class="card-body py-5">
                        <i class="bi bi-cart-x text-second" style="font-size: 4rem;"></i>
                        <h3 class="text-primary mt-3">Корзина пуста</h3>
                        <p class="text-second mb-4">Добавьте товары из каталога</p>
                        <a href="catalog" class="btn btn-accent btn-lg">Перейти в каталог</a>
                    </div>
                </div>
            </div>
        `;
    }
}

function initCartAnimations() {
    const cartItems = document.querySelectorAll('.cart-item');
    cartItems.forEach((item, index) => {
        item.style.opacity = '0';
        item.style.transform = 'translateY(20px)';
        setTimeout(() => {
            item.style.transition = 'all 0.5s ease';
            item.style.opacity = '1';
            item.style.transform = 'translateY(0)';
        }, index * 100);
    });
}

document.addEventListener('DOMContentLoaded', function() {
    initCartAnimations();

    // Инициализируем интерфейс при загрузке
    const initialCount = document.querySelectorAll('.cart-item').length;
    updateItemCount(initialCount);
});

// Дополнительная инициализация при загрузке
function initializeCartOnLoad() {
    const cartItems = document.querySelectorAll('.cart-item');
    const emptyCartMessage = document.querySelector('.empty-cart-message');
    const orderSummary = document.querySelector('aside.col-lg-4');

    if (cartItems.length === 0) {
        // Если корзина пуста с сервера
        if (emptyCartMessage) {
            emptyCartMessage.style.display = 'block';
        }
        if (orderSummary) {
            orderSummary.style.display = 'none';
        }

        // Убедимся, что секция товаров скрыта
        const cartSection = document.querySelector('section.col-lg-8');
        if (cartSection) {
            cartSection.style.display = 'none';
        }
    } else {
        // Если в корзине есть товары
        if (emptyCartMessage) {
            emptyCartMessage.style.display = 'none';
        }
    }
}

document.addEventListener('DOMContentLoaded', function() {
    initCartAnimations();
    initializeCartOnLoad();
});

// Дополнительная инициализация при загрузке
function initializeCartOnLoad() {
    const cartItems = document.querySelectorAll('.cart-item');
    const emptyCartMessage = document.querySelector('.empty-cart-message');
    const orderSummary = document.querySelector('aside.col-lg-4');

    if (cartItems.length === 0) {
        // Если корзина пуста с сервера
        if (emptyCartMessage) {
            emptyCartMessage.style.display = 'block';
            emptyCartMessage.style = 'margin-top: 48px;';
        }
        if (orderSummary) {
            orderSummary.style.display = 'none';
        }

        // Убедимся, что секция товаров скрыта
        const cartSection = document.querySelector('section.col-lg-8');
        if (cartSection) {
            cartSection.style.display = 'none';
        }
    } else {
        // Если в корзине есть товары
        if (emptyCartMessage) {
            emptyCartMessage.style.display = 'none';
        }
    }
}

document.addEventListener('DOMContentLoaded', function() {
    initCartAnimations();
    initializeCartOnLoad();
});