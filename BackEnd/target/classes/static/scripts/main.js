
    document.getElementById("image").addEventListener("change", function(event) {
        const file = event.target.files[0]; // Obtiene el archivo seleccionado
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const preview = document.getElementById("preview");
                    preview.src = e.target.result; // Asigna el contenido de la imagen
                    preview.style.display = "block"; // Muestra la imagen
                };
                reader.readAsDataURL(file); // Convierte el archivo a una URL base64
            }
        });

    function validatePasswords() {
      const password = document.getElementById('word').value;
      const confirmPassword = document.getElementById('confirmWord').value;
      const errorDiv = document.getElementById('passwordError');

      if (password !== confirmPassword) {
        errorDiv.style.display = 'block';
        return false;
      } else {
        errorDiv.style.display = 'none';
        return true;
      }
    }
    
    