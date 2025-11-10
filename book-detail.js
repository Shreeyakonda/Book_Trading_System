// Book detail page functionality
function showTradeForm() {
    const form = document.getElementById('trade-form');
    if (form) {
        form.style.display = 'block';
        const messageField = document.getElementById('message');
        if (messageField) {
            messageField.focus();
        }
    }
}

function hideTradeForm() {
    const form = document.getElementById('trade-form');
    if (form) {
        form.style.display = 'none';
    }
}

// Close form when clicking outside (optional enhancement)
document.addEventListener('DOMContentLoaded', function() {
    document.addEventListener('click', function(event) {
        const form = document.getElementById('trade-form');
        const button = document.querySelector('[onclick="showTradeForm()"]');
        
        if (form && button && 
            !form.contains(event.target) && 
            event.target !== button &&
            !button.contains(event.target)) {
            // Optional: uncomment to auto-close on outside click
            // hideTradeForm();
        }
    });
});

