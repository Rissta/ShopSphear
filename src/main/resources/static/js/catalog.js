
function validateFilters() {
    const minPrice = document.querySelector('input[name="minPrice"]').value;
    const maxPrice = document.querySelector('input[name="maxPrice"]').value;

    if (minPrice && maxPrice && parseInt(minPrice) > parseInt(maxPrice)) {
        showNotification('Минимальная цена не может быть больше максимальной', 'error');
        return false;
    }

    if (minPrice && parseInt(minPrice) < 0) {
        showNotification('Цена не может быть отрицательной', 'error');
        return false;
    }

    return true;
}

function addToCart(productId) {
    fetch('/catalog/add-to-cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'id=' + productId
    })
    .then(response => {
        if (response.ok) {
            showNotification('Товар добавлен в корзину!', 'success');
        } else {
            showNotification('Ошибка при добавлении товара', 'error');
        }
    })
    .catch(error => {
        console.error('Ошибка:', error);
        showNotification('Ошибка при добавлении товара', 'error');
    });
}

function showProductDetails(imgElement) {
    console.log('showProductDetails called', imgElement);

    const productId = imgElement.getAttribute('data-product-id');
    const productName = imgElement.getAttribute('data-product-name');
    const category = imgElement.getAttribute('data-product-category');
    const price = imgElement.getAttribute('data-product-price');
    const oldPrice = imgElement.getAttribute('data-product-old-price');
    const imageUrl = imgElement.getAttribute('data-product-image');
    const rating = imgElement.getAttribute('data-product-rating');
    const description = imgElement.getAttribute('data-product-description');

    console.log('Real Product data:', {
        productId,
        productName,
        price,
        imageUrl
    });

    document.getElementById('modalTitle').textContent = productName || 'Название товара';
    document.getElementById('modalCategory').textContent = category || 'Категория';
    document.getElementById('modalPrice').textContent = formatPrice(price) + ' руб';
    document.getElementById('modalImage').src = imageUrl || '';
    document.getElementById('modalImage').alt = productName || 'Товар';

    const oldPriceElement = document.getElementById('modalOldPrice');
    if (oldPrice && oldPrice !== 'null' && parseFloat(oldPrice) > parseFloat(price)) {
        oldPriceElement.textContent = formatPrice(oldPrice) + ' руб';
        oldPriceElement.style.display = 'inline';
    } else {
        oldPriceElement.style.display = 'none';
    }

    const ratingElement = document.getElementById('modalRating');
    ratingElement.innerHTML = '';
    const ratingValue = parseInt(rating) || 0;
    for (let i = 1; i <= 5; i++) {
        const star = document.createElement('i');
        star.className = i <= ratingValue ? 'bi bi-star-fill' : 'bi bi-star';
        ratingElement.appendChild(star);
    }

    document.getElementById('modalDescription').textContent = description || 'Описание отсутствует';

    document.getElementById('productModal').setAttribute('data-current-product-id', productId);

    const modalElement = document.getElementById('productModal');
    const modal = new bootstrap.Modal(modalElement);
    modal.show();

    return false;
}
document.addEventListener('DOMContentLoaded', function() {
    console.log('Catalog page loaded');

    document.querySelectorAll('#productsContainer button').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
        });
    });

    document.querySelectorAll('.product-image').forEach(img => {
        img.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
        });
    });
});

function formatPrice(price) {
    const numPrice = parseFloat(price);
    return isNaN(numPrice) ? '0' : numPrice.toLocaleString('ru-RU', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}



function addToCartFromModal() {
    const productId = document.getElementById('productModal').getAttribute('data-current-product-id');
    if (productId) {
        addToCart(productId);
        const modalElement = document.getElementById('productModal');
        const modal = bootstrap.Modal.getInstance(modalElement);
        modal.hide();
    } else {
        showNotification('Не удалось добавить товар в корзину', 'error');
    }
}

document.addEventListener('DOMContentLoaded', function() {
    console.log('Catalog page loaded');
});