import React, { useState, useEffect } from "react";
import './Home.css';
import Typography from '@mui/material/Typography';


export default function HomePage() {

  const [rotateValue, setRotateValue] = useState(0);

  // פונקציה לסיבוב העיגול
  const rotateCircle = (degrees) => {
    const newValue = rotateValue + degrees;
    setRotateValue(newValue);
  };

  //   פונקציה לתנועה אוטומטית כל כמה שניות
  useEffect(() => {
    const interval = setInterval(() => {
      rotateCircle(90); // מעביר 90 מעלות בכל פעם
    }, 2000); // כל 2 שניות

    return () => clearInterval(interval); // ניקוי פעולה בהרצת רק מתי שהרכיב מוסר
  }, [rotateValue]); // מוסיף את rotateValue לתנאי השימוש ב־useEffect

  return (
    <>
      <div className="main">
        <div className="information">
          <div className="overlay"></div>
          <img src="src/pic/camera.jfif" className="mobile"></img>
          <div className="circle" style={{ transform: `rotate(${rotateValue}deg)` }}>
            <div className="feature one">
              <div>
                <Typography
                  variant="h4">שיתופים
                  <Typography variant="h6">jnc</Typography>
                </Typography>
              </div>
            </div>

            <div className="feature two">
              <div>
                <Typography
                  variant="h4">אביזרים
                  <Typography variant="h6">מעבר למצלמה: אביזרים מקצועיים לשדרוג תמונה מושלמת</Typography>
                </Typography>
              </div>
            </div>

            <div className="feature three">
              <div>
                <Typography
                  variant="h4">לוקיישנים
                  <Typography variant="h6">גלו חופים ונופים מדהימים לצילום מושלם."</Typography>
                </Typography>
              </div>
            </div>

            <div className="feature four">
              <div>
                <Typography
                  variant="h4">טיפים
                  <Typography variant="h6">גלו טריקים מקצועיים עבור צילום מושלם: הדרכות, רעיונות וטיפים</Typography>
                </Typography>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

