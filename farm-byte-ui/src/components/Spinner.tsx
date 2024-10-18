

const Spinner = () => {
    return (
        <div>
            <div
                className="absolute top-0 flex flex-row justify-center items-center h-screen w-screen bg-foreground/10 z-40">
                <p className="material-symbols-outlined animate-spin text-5xl w-full text-center text-primary">progress_activity</p>
            </div>
        </div>
    );
};

export default Spinner;