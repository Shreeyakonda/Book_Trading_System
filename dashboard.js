// Dashboard functionality
function showTab(tabName, element) {
    // Hide all tab contents
    const tabContents = document.querySelectorAll('.tab-content');
    tabContents.forEach(tab => {
        tab.classList.remove('active');
    });

    // Remove active class from all tab buttons
    const tabButtons = document.querySelectorAll('.tab-btn');
    tabButtons.forEach(btn => {
        btn.classList.remove('active');
    });

    // Show selected tab content
    const selectedTab = document.getElementById(tabName);
    if (selectedTab) {
        selectedTab.classList.add('active');
    }

    // Add active class to clicked button
    if (element) {
        element.classList.add('active');
    }
}

// Initialize first tab on page load
document.addEventListener('DOMContentLoaded', function() {
    // Ensure first tab is shown
    const firstTabContent = document.querySelector('.tab-content.active');
    if (!firstTabContent) {
        const firstTab = document.querySelector('.tab-btn');
        if (firstTab) {
            firstTab.click();
        }
    }
});

