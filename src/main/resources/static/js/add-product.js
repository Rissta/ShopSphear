  document.getElementById('imageUrl').addEventListener('input', function() {
      const preview = document.getElementById('imagePreview');
      const previewText = document.getElementById('previewText');
      const url = this.value;

      if (url) {
          preview.src = url;
          preview.classList.remove('d-none');
          previewText.classList.add('d-none');
      } else {
          preview.classList.add('d-none');
          previewText.classList.remove('d-none');
      }
  });

  function clearForm() {
      document.querySelector('.product-form').reset();
      document.getElementById('imagePreview').classList.add('d-none');
      document.getElementById('previewText').classList.remove('d-none');
  }

  document.addEventListener('DOMContentLoaded', function() {
      const dateField = document.getElementById('createdAt');
      if (!dateField.value) {
          const today = new Date().toISOString().split('T')[0];
          dateField.value = today;
      }
  });