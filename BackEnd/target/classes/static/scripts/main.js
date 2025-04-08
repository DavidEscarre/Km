
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
