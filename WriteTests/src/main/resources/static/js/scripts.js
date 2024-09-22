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

// Function to add a new question dynamically
function addQuestion() {
    const container = document.getElementById('questions-container');
    const questionCount = container.children.length; // Keeps track of question index

    const questionHtml = `
        <div class="question-item">
            <label>Question:</label>
            <input type="text" name="questions[${questionCount}].questionText" required>
            <label>Type:</label>
            <select name="questions[${questionCount}].questionType" onchange="toggleAnswerType(this, ${questionCount})">
                <option value="MULTIPLE_CHOICE">Multiple Choice</option>
                <option value="TRUE_FALSE">True/False</option>
            </select>
            <div class="answers-container" id="answers-container-${questionCount}">
                <!-- Dynamic answers will be added here -->
            </div>
            <button type="button" onclick="addAnswer(${questionCount})">Add Answer</button>
        </div>
    `;
    container.insertAdjacentHTML('beforeend', questionHtml);
}

// Function to toggle between multiple choice and true/false answers
function toggleAnswerType(selectElement, questionIndex) {
    const answerContainer = document.getElementById(`answers-container-${questionIndex}`);
    answerContainer.innerHTML = ''; // Clear existing answers

    if (selectElement.value === 'TRUE_FALSE') {
        // Add true/false options
        const trueFalseHtml = `
            <div>
                <label>True</label>
                <input type="radio" name="questions[${questionIndex}].correctAnswer" value="true" required>
                <label>False</label>
                <input type="radio" name="questions[${questionIndex}].correctAnswer" value="false" required>
            </div>
        `;
        answerContainer.insertAdjacentHTML('beforeend', trueFalseHtml);
    }
}

// Function to add a new answer for multiple choice questions
function addAnswer(questionIndex) {
    const container = document.getElementById(`answers-container-${questionIndex}`);
    const answerCount = container.children.length;

    const answerHtml = `
    <div class="answer-item">
        <label>Answer:</label>
        <input type="text" name="questions[${questionIndex}].answers[${answerCount}].answerText" required>
        <label>Correct:</label>
        <input type="checkbox" name="questions[${questionIndex}].answers[${answerCount}].isCorrect" value="true">
        <!-- Add a hidden field for unchecked checkboxes -->
        <input type="hidden" name="questions[${questionIndex}].answers[${answerCount}].isCorrect" value="false">
    </div>
    `;
    container.insertAdjacentHTML('beforeend', answerHtml);
}
