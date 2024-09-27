import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import {BrowserRouter as Router} from "react-router-dom";
import AppRoute from "@/AppRoute.tsx";
import './global.css'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Router>
        <AppRoute/>
    </Router>
  </StrictMode>,
)
