// Registration form validation
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registerForm');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');

    if (form) {
        form.addEventListener('submit', function(event) {
            // Client-side password match validation
            if (password.value !== confirmPassword.value) {
                event.preventDefault();
                alert('Passwords do not match!');
                confirmPassword.focus();
                return false;
            }

            // Password length validation
            if (password.value.length < 6) {
                event.preventDefault();
                alert('Password must be at least 6 characters long!');
                password.focus();
                return false;
            }
        });

        // Real-time password match indicator
        if (confirmPassword) {
            confirmPassword.addEventListener('input', function() {
                if (password.value !== confirmPassword.value) {
                    confirmPassword.setCustomValidity('Passwords do not match');
                } else {
                    confirmPassword.setCustomValidity('');
                }
            });
        }
    }
});

