# Eco Assistant Integration Guide

This document describes how to run and use the Eco Assistant (chatbot + recommendation service) that lives in the `model/` directory and is consumed by the React dashboard.

## 1. Overview

- **Service location:** `model/`
- **Tech stack:** Flask, pandas, scikit-learn
- **Purpose:** Provide natural language assistance, product recommendations, order-tracking responses, and eco tips.
- **Frontend entry point:** Floating "Eco Assistant" widget available to customer users inside the dashboard (see `FrontEnd/src/components/AssistantWidget.jsx`).

## 2. Prerequisites

| Component | Requirement |
| --- | --- |
| Python | 3.10+ |
| Node.js | matches existing frontend setup |
| Spring Boot backend | Running on `http://localhost:8080` (or update URLs accordingly) |

## 3. Configure Environment Variables

### model/.env
Create `model/.env` from the provided example:
```bash
cd model
cp .env.example .env
```
Edit the values if needed:
```
BACKEND_API_URL=http://localhost:8080   # Spring Boot API
FRONTEND_URL=http://localhost:5173      # React dev server or deployed origin
FLASK_RUN_PORT=5000                    # optional override
```

### FrontEnd/.env
Create `FrontEnd/.env` from the example:
```bash
cd FrontEnd
cp .env.example .env
```
Set the URL where the Flask service will run:
```
VITE_AI_SERVICE_URL=http://localhost:5000
```

## 4. Run the Services

1. **Start Spring Boot backend** (from repo root):
   ```bash
   ./mvnw spring-boot:run
   # or mvnw.cmd on Windows
   ```

2. **Start the AI service**:
   ```bash
   cd model
   python -m venv .venv
   . .venv/Scripts/activate   # Windows
   pip install -r requirements.txt
   python -m app.flask_app
   ```
   The server logs will show how many products were loaded and which backend URL is being used.

3. **Start the React frontend**:
   ```bash
   cd FrontEnd
   npm install    # first time only
   npm run dev
   ```

## 5. Using the Assistant in the UI

- Log in as a customer or regular user.
- A green "Eco Assistant" button appears in the lower-right corner (rendered by `DashboardLayout`).
- Click it to open the chat panel.
- The widget automatically checks the AI service health and fetches starter recommendations.
- Type messages such as:
  - "Recommend something like bamboo bottle"
  - "Track order 12345"
  - "Give me an eco tip"
  - "I want to return an item"
- Responses are streamed back through the Flask `/chat` endpoint and displayed in the panel.

## 6. API Endpoints (Model Service)

| Method | Path | Description |
| --- | --- | --- |
| `GET /health` | Returns service status, backend URL, product count |
| `POST /chat` | Expects `{ message, user_id? }` and returns chatbot text |
| `GET /recommend?product_name=` | Returns similar products |
| `GET /products` | Returns cached product list (for debugging) |
| `GET /` | Minimal HTML test client |

## 7. Troubleshooting

| Symptom | Fix |
| --- | --- |
| Frontend widget says "Assistant is offline" | Ensure `model/app/flask_app.py` is running, check `VITE_AI_SERVICE_URL` | 
| Flask logs show "Could not fetch from backend" | Confirm Spring Boot backend URL in `BACKEND_API_URL` is reachable |
| CORS errors in browser console | Verify `FRONTEND_URL` matches the React origin (including port) |
| Recommendations list is empty | Make sure `/api/products` returns data; otherwise the service falls back to mock products |

## 8. Extending the Assistant

- **Custom intents:** Modify `model/app/chatbot/bot.py` to add new intent detection rules.
- **Product fields:** Expand `DynamicCatalogManager` to pull additional metadata (e.g., carbon scores) and retrain the recommender using those features.
- **UI behavior:** Adjust `AssistantWidget.jsx` to support attachments, long responses, or persistence.

For deeper details, refer to:
- `model/README.md` – service internals
- `FrontEnd/src/components/AssistantWidget.jsx` – UI logic
- `FrontEnd/src/services/chatbotService.js` – HTTP client wrapper
