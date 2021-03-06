import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./SongSelection.css";
import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";
import { capitalizeFirstLetter } from "../../../global/helper";

import { SessionButton } from "../../components";

function SongSelection(props) {
  const [showContent, setShowContent] = useState(false);
  const [showDetails, setShowDetails] = useState(false);

  useEffect(() => {
    if (props.song !== null) {
      setShowContent(true);
    }
    if (props.songMeta !== null) {
      setShowDetails(true);
    }
  }, [props.song, props.songMeta]);

  const mapping = [
    "acousticness",
    "danceability",
    "energy",
    "instrumentalness",
    "note",
    "liveness",
    "loudness",
    "speechiness",
    "tempo",
  ];

  return (
    <React.Fragment>
      <div className="selected-song-main-info">
        {props.song !== undefined ? (
          <SessionButton
            style={{top: "24px"}}
            isSelected={props.isInSession}
            onClick={() => {
              if (props.isInSession) {
                props.removeFromSession(props.song.id, "track");
              } else {
                props.addToSession(props.song.id, "track");
              }
            }}
          />
        ) : null}
        <div className="selected-song-song-art">
          {showContent ? (
            <img
              className={"fade-in"}
              alt="song art"
              src={props.song.album.image}
            />
          ) : (
            <SkeletonPulse></SkeletonPulse>
          )}
        </div>
        <div className="selected-song-details">
          <h2>
            {showContent ? (
              <div className="fade-in">{props.song.name}</div>
            ) : (
              <SkeletonPulse style={{ width: "160px", height: "24px" }} />
            )}
          </h2>
          <h3>
            {showContent ? (
              <Link
                to={`/app/explore/albums/${props.song.album.id}`}
                className="fade-in"
              >{`${props.song.album.name} (${props.song.album.release_date})`}</Link>
            ) : (
              <SkeletonPulse style={{ width: "160px", height: "24px" }} />
            )}
          </h3>
          <p>
            {showContent ? (
              <div className="fade-in">
                {props.song.artists.map(function (artist, index) {
                  return index < props.song.artists.length - 1 ? (
                    <Link
                      to={`/app/explore/artists/${artist.id}`}
                      key={index}
                    >{`${artist.name}, `}</Link>
                  ) : (
                    <Link to={`/app/explore/artists/${artist.id}`} key={index}>
                      {artist.name}
                    </Link>
                  );
                })}
              </div>
            ) : (
              <SkeletonPulse style={{ width: "256px", height: "24px" }} />
            )}
          </p>
        </div>
      </div>
      {showDetails ? (
        <div className="selected-song-extra-info">
          {(showContent
            ? mapping
                .map(function (attr, index) {
                  if (props.songMeta[attr]) {
                    return (
                      capitalizeFirstLetter(attr) + ": " + props.songMeta[attr]
                    );
                  }
                })
                .filter((song) => song)
            : [1, 2, 3, 4, 5, 6, 7, 8, 9]
          ).map(function (attribute, index) {
            return (
              <div
                className={"fade-in selected-song-extra-info-item"}
                style={{ animationDelay: index / 9 + "s" }}
                key={index}
              >
                {showContent ? (
                  <div className="fade-in">{attribute}</div>
                ) : (
                  <SkeletonPulse></SkeletonPulse>
                )}
              </div>
            );
          })}
        </div>
      ) : null}
    </React.Fragment>
  );
}

export default SongSelection;
