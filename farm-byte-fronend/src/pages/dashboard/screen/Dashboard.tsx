
const Dashboard = () => {
    return (
        <div className="text-3xl text-amber-700">
            DASHBOARD<br/>
            {import.meta.env.VITE_API_BACKEND_URL}
            <br/>
            {import.meta.env.VITE_APP_TITLE}
        </div>
    );
}

export default Dashboard;