import React from "react";
import "./SongCard.css";

import axios from "axios";
import { Link } from "react-router-dom";

import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";
import { isOverCardLimit, truncateTitle } from "../../../global/helper";
import { SessionButton } from "../../components";
import api from "../../../utils/api";

function SongCard(props) {
  return (
    <div
      className="song-card"
      style={props.style}
      onClick={() => props.onClick()}
    >
      <div className="song-card-margin">
        {props.skeletonPulse === undefined ? (
          <SessionButton
            isSelected={props.isInSession}
            onClick={() => {
              if (props.isInSession) {
                props.removeFromSession(props.song.id);
              } else {
                props.addToSession(props.song.id);
              }
            }}
          />
        ) : null}
        <div className="song-card-album-art">
          {props.skeletonPulse === undefined ? (
            <img alt="album art" src={props.song.album.image}></img>
          ) : (
            <SkeletonPulse></SkeletonPulse>
          )}
        </div>
        <div className="song-card-info">
          {props.skeletonPulse === undefined ? (
            <h2>{truncateTitle(props.song.name, 52)}</h2>
          ) : (
            <SkeletonPulse
              style={{ width: "128px", height: "24px", marginBottom: "8px" }}
            />
          )}
          <div
            className={
              isOverCardLimit(props.song.name)
                ? "shift-card-content"
                : "no-shift-content"
            }
          >
            {props.skeletonPulse === undefined ? (
              <Link to={"/app/explore/artists/" + props.song.artists[0].id}>
                <p>
                  {props.song.artists.map(function (artist, index) {
                    return index < props.song.artists.length - 1
                      ? artist.name + ", "
                      : artist.name;
                  })}
                </p>
              </Link>
            ) : (
              <SkeletonPulse
                style={{ width: "200px", height: "20px", marginBottom: "8px" }}
              />
            )}
            {props.skeletonPulse === undefined ? (
              <p className="subtitle">
                Playlist Count: {props.song.popularity}
              </p>
            ) : (
              <SkeletonPulse
                style={{ width: "81px", height: "12px", marginBottom: "8px" }}
              />
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default SongCard;
