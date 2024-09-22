// scripts.js

document.querySelector('form').addEventListener('submit', function(event) {
    // Get all checkboxes in the form
    const checkboxes = document.querySelectorAll('.correct-checkbox');

    checkboxes.forEach(checkbox => {
        // If checkbox is unchecked, add a hidden input to send false
        if (!checkbox.checked) {
            const hiddenFalseInput = document.createElement('input');
            hiddenFalseInput.type = 'hidden';
            hiddenFalseInput.name = checkbox.name;
            hiddenFalseInput.value = 'false';
            checkbox.parentElement.appendChild(hiddenFalseInput);
        } else {
            // If checked, ensure the checkbox sends true
            checkbox.value = 'true';
        }
    });
});




function addQuestion() {
    const container = document.getElementById('questions-container');

    const questionCount = container.children.length; // Keeps track of question index
    const questionHtml = `
        <div class="question-item">
            <label>Question:</label>
            <input type="text" name="questions[${questionCount}].questionText" required>
            <label>Type:</label>
            <select name="questions[${questionCount}].questionType">
                <option value="MULTIPLE_CHOICE">Multiple Choice</option>
                <option value="TRUE_FALSE">True/False</option>
            </select>
            <div class="answers-container">
                <button type="button" onclick="addAnswer(this, ${questionCount})">Add Answer</button>
            </div>
        </div>
    `;
    container.insertAdjacentHTML('beforeend', questionHtml);
}

function addAnswer(button,questionIndex) {
    const container = button.parentElement;
    const answerCount = container.children.length - 1;

    const answerHtml = `
    <div class="answer-item">
        <label>Answer:</label>
        <input type="text" name="questions[${questionIndex}].answers[${answerCount}].answerText" required>
        <!-- Hidden input to submit false if checkbox is not checked -->
        <input type="hidden" name="questions[${questionIndex}].answers[${answerCount}].IsCorrect" value="false">
        <label>Correct:</label>
        <input type="checkbox" th:field="*{questions[${questionIndex}].answers[${answerCount}].isCorrect}" value="True">
    </div>
`;
    container.insertAdjacentHTML('beforeend', answerHtml);
}
